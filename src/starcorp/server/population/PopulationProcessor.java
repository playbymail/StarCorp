/**
 *  Copyright 2007 Seyed Razavi
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at 
 *
 *  http://www.apache.org/licenses/LICENSE-2.0 
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *  See the License for the specific language governing permissions and limitations under the License. 
 */
package starcorp.server.population;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import starcorp.common.entities.AColonists;
import starcorp.common.entities.ColonistGrant;
import starcorp.common.entities.Colony;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.Facility;
import starcorp.common.entities.MarketItem;
import starcorp.common.entities.Planet;
import starcorp.common.entities.StarSystem;
import starcorp.common.entities.Unemployed;
import starcorp.common.entities.Workers;
import starcorp.common.types.AFacilityType;
import starcorp.common.types.AItemType;
import starcorp.common.types.CashTransaction;
import starcorp.common.types.ConsumerGoods;
import starcorp.common.types.CoordinatesPolar;
import starcorp.common.types.Population;
import starcorp.common.types.PopulationClass;
import starcorp.common.types.ServiceFacility;
import starcorp.server.ServerConfiguration;
import starcorp.server.entitystore.IEntityStore;

/**
 * starcorp.server.population.PopulationProcessor
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 17 Sep 2007
 */
public class PopulationProcessor {

	public static final int MAX_BIRTH_PERCENT = 10;
	
	public static final double HAPPINESS_FOOD = 10.0;
	public static final double HAPPINESS_DRINK = 10.0;
	public static final double HAPPINESS_INTOXICANT = 15.0;
	public static final double HAPPINESS_CLOTHES = 15.0;
	public static final double HAPPINESS_MEDICAL = 10.0;
	public static final double HAPPINESS_FITNESS = 10.0;
	public static final double HAPPINESS_ENTERTAINMENT = 15.0;
	public static final double HAPPINESS_EDUCATION = 15.0;
	
	public static final double MIGRATION_DISTANCE_SAME_PLANET = 2.0;
	public static final double MIGRATION_DISTANCE_SAME_LOCATION = 1.0;
	public static final double MIGRATION_DISTANCE_SAME_SYSTEM = 0.5;
	public static final double MIGRATION_DISTANCE_OTHER_SYSTEM = 0.2;
	
	private static Random rnd = new Random(System.currentTimeMillis());
	
	private IEntityStore entityStore;
	
	public PopulationProcessor(IEntityStore store) {
		this.entityStore = store;
	}
	
	public void process() {
		
		List<?> colonies = entityStore.listColonies();
		
		Iterator<?> i = colonies.iterator();
		while(i.hasNext()) {
			Colony colony = (Colony) i.next();
			process(colony);
		}
	}
	
	private void process(Colony colony) {
		Iterator<String> k = PopulationClass.typeKeys();
		while(k.hasNext()) {
			PopulationClass popClass = PopulationClass.getType(k.next());
			List<?> sorted = entityStore.listFacilitiesBySalary(colony, popClass);
			Iterator<?> i = sorted.iterator();
			while(i.hasNext()) {
				Facility facility = (Facility) i.next();
				// employment
				hireWorkers(facility,popClass);
				Workers workers = entityStore.getWorkers(facility, popClass);
				if(workers == null) {
					continue;
				}
				quitWorkers(workers);
				payWorkers(workers);
			}
			// meet needs
			List<?> workers = entityStore.listWorkers(colony, popClass);
			Iterator<?> w = workers.iterator();
			while(w.hasNext()) {
				AColonists worker = (AColonists) w.next();
				worker.setHappiness(0.0);
				payGrants(worker);
				doConsumerNeeds(worker);
				doServiceNeeds(worker);
			}
			Unemployed unemployed = entityStore.getUnemployed(colony, popClass);
			unemployed.setHappiness(0.0);
			payGrants(unemployed);
			doConsumerNeeds(unemployed);
			doServiceNeeds(unemployed);
			
			doMigration(unemployed);
		}
		doBirths(colony);
		doDeaths(colony);
	}
	
	private void hireWorkers(Facility facility, PopulationClass popClass) {
		Population required = facility.getTypeClass().getWorkerRequirement(popClass);
		Workers workers = entityStore.getWorkers(facility, popClass);
		if(workers == null || workers.getPopulation().getQuantity() < required.getQuantity()) {
			// more needed so hire any unemployed
			Unemployed unemployed = entityStore.getUnemployed(facility.getColony(), popClass);
			if(unemployed != null) {
				int qty = required.getQuantity() - workers.getPopulation().getQuantity();
				if(qty < unemployed.getQuantity()) {
					qty = unemployed.getQuantity();
				}
				if(workers == null) {
					workers = new Workers();
					workers.setPopulation(new Population());
					workers.getPopulation().setPopClass(popClass);
					workers.setColony(facility.getColony());
					workers.setFacility(facility);
					workers.setSalary(0); // we set salary to zero as the owner hasn't specified one
					entityStore.save(workers);
				}
				int cash = qty * unemployed.getCashPerPerson();
				workers.addPopulation(qty);
				unemployed.removePopulation(qty);
				workers.addCash(cash);
				unemployed.removeCash(cash);
			}
		}
	}
	
	private void quitWorkers(Workers workers) {
		Facility facility = workers.getFacility();
		double quitChance = 0.0;
		double efficiency = facility.getEfficiency(entityStore.listWorkers(facility));
		quitChance += (100.0 - efficiency);
		quitChance += (100.0 - workers.getHappiness());
		int quitters = 0;
		int total = workers.getPopulation().getQuantity();
		for(int i = 0; i < total; i++) {
			int rand = rnd.nextInt(100) + 1;
			if(rand >= quitChance) {
				quitters++;
			}
		}
		Unemployed unemployed = entityStore.getUnemployed(facility.getColony(), workers.getPopClass());
		int cash = quitters * workers.getCashPerPerson();
		workers.removePopulation(quitters);
		unemployed.addPopulation(quitters);
		workers.removeCash(cash);
		unemployed.addCash(cash);
	}
	
	private void payWorkers(Workers workers) {
		Corporation employer = workers.getFacility().getOwner();
		int salaryAvail = employer.getCredits() / workers.getSalary();
		int qty = workers.getPopulation().getQuantity();
		if(salaryAvail < qty) {
			int quit = qty - salaryAvail;
			int redundancy = (quit * workers.getSalary());
			Unemployed unemployed = entityStore.getUnemployed(workers.getColony(), workers.getPopulation().getPopClass());
			if(unemployed == null) {
				unemployed = new Unemployed();
				unemployed.setPopulation(new Population(workers.getPopClass()));
				unemployed.setColony(workers.getColony());
				entityStore.save(unemployed);
			}
			unemployed.addPopulation(quit);
			workers.removePopulation(quit);
			Object[] args = {workers.getPopulation().getPopClass().getName(), String.valueOf(quit)};
			String desc = CashTransaction.getDescription(CashTransaction.REDUNDANCY_PAY, args);
			employer.remove(redundancy, ServerConfiguration.getCurrentDate(), desc);
			unemployed.addCash(redundancy);
			qty -= quit;
		}
		int salaryPaid = (qty * workers.getSalary());
		Object[] args = {workers.getPopulation().getPopClass().getName(), String.valueOf(qty)};
		String desc = CashTransaction.getDescription(CashTransaction.SALARY_PAID, args);
		employer.remove(salaryPaid, ServerConfiguration.getCurrentDate(), desc);
		workers.addCash(salaryPaid);
	}
	
	private void payGrants(AColonists colonists) {
		ColonistGrant grant = entityStore.getColonistGrant(colonists.getColony(), colonists.getPopulation().getPopClass(), true);
		if(grant != null) {
			Corporation govt = grant.getColony().getGovernment();
			int cash = grant.getCredits() * colonists.getQuantity();
			if(cash < govt.getCredits()) {
				cash = govt.getCredits();
			}
			Object[] args = {colonists.getPopClass().getName(), String.valueOf(colonists.getQuantity())};
			String desc = CashTransaction.getDescription(CashTransaction.GRANT_PAID, args);
			govt.add(cash, ServerConfiguration.getCurrentDate(), desc);
			colonists.addCash(cash);
		}
	}
	
	private void doConsumerNeeds(AColonists colonists, List<AItemType> types, double happinessRating) {
		Colony colony = colonists.getColony();
		List<?> market = entityStore.listMarket(colony, types, 0);
		MarketItem.BuyResult result = MarketItem.buy(ServerConfiguration.getCurrentDate(), market, colonists.getQuantity(), colonists.getCash());
		
		double ratio = (double) colonists.getQuantity() / (double) result.quantityBought;
		double happiness = ratio * happinessRating;
		
		colonists.removeCash(result.totalPrice);
		colonists.addHappiness(happiness);
	}
	
	private void doConsumerNeeds(AColonists colonists) {
		int quality = colonists.getPopClass().getConsumerQualityRequired();
		List<AItemType> foodTypes = ConsumerGoods.listFood(quality);
		doConsumerNeeds(colonists, foodTypes, HAPPINESS_FOOD);
		List<AItemType> drinkTypes = ConsumerGoods.listDrink(quality);
		doConsumerNeeds(colonists, drinkTypes, HAPPINESS_DRINK);
		List<AItemType> intoxicantTypes = ConsumerGoods.listIntoxicant(quality);
		doConsumerNeeds(colonists, intoxicantTypes, HAPPINESS_INTOXICANT);
		List<AItemType> clothesTypes = ConsumerGoods.listClothes(quality);
		doConsumerNeeds(colonists, clothesTypes, HAPPINESS_CLOTHES);
	}
	
	private void doServiceNeeds(AColonists colonists, List<AFacilityType> types, double happinessRating) {
		Colony colony = colonists.getColony();
		Map<Facility, List<?>> facilities = entityStore.listFacilitiesWithWorkers(colony, types);
		Facility.ServiceResult result = Facility.service(ServerConfiguration.getCurrentDate(),facilities, colonists.getQuantity(), colonists.getCash());
		
		double ratio = (double) colonists.getQuantity() / (double) result.quantityServiced;
		double happiness = ratio * happinessRating;
		
		colonists.removeCash(result.totalCost);
		colonists.addHappiness(happiness);
	}
	
	private void doServiceNeeds(AColonists colonists) {
		int quality = colonists.getPopClass().getServiceQualityRequired();
		List<AFacilityType> medicalTypes = ServiceFacility.listMedical(quality);
		doServiceNeeds(colonists, medicalTypes, HAPPINESS_MEDICAL);
		List<AFacilityType> fitnessTypes = ServiceFacility.listFitness(quality);
		doServiceNeeds(colonists, fitnessTypes, HAPPINESS_FITNESS);
		List<AFacilityType> entTypes = ServiceFacility.listEntertainment(quality);
		doServiceNeeds(colonists, entTypes, HAPPINESS_ENTERTAINMENT);
		List<AFacilityType> eduTypes = ServiceFacility.listEducation(quality);
		doServiceNeeds(colonists, eduTypes, HAPPINESS_EDUCATION);
	}
	
	private void doBirths(Colony colony) {
		int total = AColonists.countColonists(entityStore.listColonists(colony));
		double birthRate = rnd.nextInt(MAX_BIRTH_PERCENT + 1) / 100.0;
		int births = (int) (total * birthRate);
		int typeCount = PopulationClass.countTypes();
		int birthsPerClass = births / typeCount;
		Iterator<String> i = PopulationClass.typeKeys();
		while(i.hasNext()) {
			String typeKey = i.next();
			PopulationClass popClass = PopulationClass.getType(typeKey);
			
			Unemployed colonists = entityStore.getUnemployed(colony, popClass);
			if(colonists == null) {
				colonists = new Unemployed();
				colonists.setCash(0);
				colonists.setColony(colony);
				colonists.setHappiness(0.0);
				colonists.setPopulation(new Population(popClass));
				entityStore.save(colonists);
			}
			
			colonists.addPopulation(birthsPerClass);
		}
	}
	
	private void doDeaths(Colony colony) {
		int total = AColonists.countColonists(entityStore.listColonists(colony));
		double deathRate = colony.getHazardLevel();
		int deaths = (int) (total * deathRate);
		int typeCount = PopulationClass.countTypes();
		int deathsPerClass = deaths / typeCount;
		Iterator<String> i = PopulationClass.typeKeys();
		while(i.hasNext()) {
			String typeKey = i.next();
			PopulationClass popClass = PopulationClass.getType(typeKey);
			
			Unemployed colonists = entityStore.getUnemployed(colony, popClass);
			int killed = 0;
			int qty = deathsPerClass;
			if(colonists != null) {
				if(qty > colonists.getQuantity()) {
					qty = colonists.getQuantity();
				}
				colonists.removePopulation(qty);
				killed += qty;
			}
			
			if(killed < deathsPerClass) {
				List<?> workers = entityStore.listWorkers(colony);
				int size = workers.size();
				while(size > 0 && killed < deathsPerClass) {
					int random = rnd.nextInt(size);
					Workers w = (Workers) workers.get(random);
					qty = (deathsPerClass - killed) / size;
					int mod = (deathsPerClass - killed) % size; 
					if(size == 1) {
						qty += mod;
					}
					if(qty > w.getQuantity()) {
						qty = w.getQuantity();
					}
					w.removePopulation(qty);
					if(w.getQuantity() < 1) {
						workers.remove(random);
					}
					size = workers.size();
				}
			}
		}
	}

	private void doMigration(Unemployed unemployed, Colony colony, double distanceModifier) {
		if(unemployed.getQuantity() < 1)
			return;
		List<?> colonists = entityStore.listColonists(colony, unemployed.getPopClass());
		double happiness = AColonists.getAverageHappiness(colonists);
		
		double migrationRate = unemployed.getHappiness() - happiness;
		migrationRate *= distanceModifier;
		
		int migrate = (int) (unemployed.getQuantity() * migrationRate);
		
		if(migrate > 0) {
			unemployed.removeCash(migrate * unemployed.getCashPerPerson());
			unemployed.removePopulation(migrate);
			Unemployed other = entityStore.getUnemployed(colony, unemployed.getPopClass());
			if(other == null) {
				other = new Unemployed();
				other.setCash(0);
				other.setColony(colony);
				other.setHappiness(0.0);
				other.setPopulation(new Population(unemployed.getPopClass()));
				entityStore.save(other);
			}
			other.addPopulation(migrate);
		}
	}
	
	private void doMigration(Unemployed unemployed, List<?> colonies, double distanceModifier) {
		if(unemployed.getQuantity() < 1)
			return;
		Iterator<?> i = colonies.iterator();
		while(i.hasNext()) {
			doMigration(unemployed, (Colony) i.next(), distanceModifier);
		}
	}

	private void doMigration(Unemployed unemployed) {
		if(unemployed.getQuantity() < 1)
			return;
		Planet planet = unemployed.getColony().getPlanet();
		StarSystem system = planet.getSystem();
		CoordinatesPolar location = planet.getLocation();
		List<?> samePlanet = entityStore.listColonies(planet);
		List<?> sameLocation = entityStore.listColonies(system, location, planet);
		List<?> sameSystem = entityStore.listColonies(system, location);
		List<?> others = entityStore.listColonies(system);
		
		doMigration(unemployed, samePlanet, MIGRATION_DISTANCE_SAME_PLANET);
		doMigration(unemployed, sameLocation, MIGRATION_DISTANCE_SAME_LOCATION);
		doMigration(unemployed, sameSystem, MIGRATION_DISTANCE_SAME_SYSTEM);
		doMigration(unemployed, others, MIGRATION_DISTANCE_OTHER_SYSTEM);
	}
}
