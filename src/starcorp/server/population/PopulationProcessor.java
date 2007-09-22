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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

	public static final double HAPPINESS_CLOTHES = 15.0;

	public static final double HAPPINESS_DRINK = 10.0;
	public static final double HAPPINESS_EDUCATION = 15.0;
	public static final double HAPPINESS_ENTERTAINMENT = 15.0;
	public static final double HAPPINESS_FITNESS = 10.0;
	public static final double HAPPINESS_FOOD = 10.0;
	public static final double HAPPINESS_INTOXICANT = 15.0;
	public static final double HAPPINESS_MEDICAL = 10.0;
	private static Log log = LogFactory.getLog(PopulationProcessor.class);

	public static final int MAX_BIRTH_PERCENT = 10;
	public static final double MIGRATION_DISTANCE_OTHER_SYSTEM = 0.2;
	public static final double MIGRATION_DISTANCE_SAME_LOCATION = 1.0;
	public static final double MIGRATION_DISTANCE_SAME_PLANET = 2.0;

	public static final double MIGRATION_DISTANCE_SAME_SYSTEM = 0.5;
	private static Random rnd = new Random(System.currentTimeMillis());

	private IEntityStore entityStore;

	public PopulationProcessor(IEntityStore store) {
		this.entityStore = store;
	}

	private void doBirths(AColonists colonist) {
		double birthRate = rnd.nextInt(MAX_BIRTH_PERCENT + 1) / 100.0;
		int births = (int) (colonist.getQuantity() * birthRate);
		if (births > 0) {
			Unemployed unemployed = entityStore.getUnemployed(colonist
					.getColony(), colonist.getPopClass());
			if (unemployed == null) {
				unemployed = new Unemployed();
				unemployed.setCash(0);
				unemployed.setColony(colonist.getColony());
				unemployed.setHappiness(0.0);
				unemployed
						.setPopulation(new Population(colonist.getPopClass()));
				entityStore.save(unemployed);
			}
			unemployed.addPopulation(births);
			if (log.isDebugEnabled())
				log.debug(births + " of " + colonist.getPopClass() + " born.");
		}

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

	private void doConsumerNeeds(AColonists colonists, List<AItemType> types,
			double happinessRating) {
		Colony colony = colonists.getColony();
		List<?> market = entityStore.listMarket(colony, types, 0);
		MarketItem.BuyResult result = MarketItem.buy(ServerConfiguration
				.getCurrentDate(), market, colonists.getQuantity(), colonists
				.getCash());

		double ratio = (double) colonists.getQuantity()
				/ (double) result.quantityBought;
		double happiness = ratio * happinessRating;

		colonists.removeCash(result.totalPrice);
		colonists.addHappiness(happiness);
		if (log.isDebugEnabled())
			log.debug(colonists + " bought " + result.quantityBought
					+ " consumer goods");
	}

	private void doDeaths(AColonists colonist) {
		double deathRate = colonist.getColony().getHazardLevel();
		int deaths = (int) (colonist.getQuantity() * deathRate);
		if (deaths > 0) {
			colonist.removePopulation(deaths);
			if (log.isDebugEnabled())
				log.debug(deaths + " of " + colonist.getPopClass() + " died.");
		}

	}

	private void doMigration(Unemployed unemployed) {
		if (unemployed.getQuantity() < 1)
			return;
		Planet planet = unemployed.getColony().getPlanet();
		StarSystem system = planet.getSystem();
		CoordinatesPolar location = planet.getLocation();
		List<?> samePlanet = entityStore.listColonies(planet);
		List<?> sameLocation = entityStore.listColonies(system, location,
				planet);
		List<?> sameSystem = entityStore.listColonies(system, location);
		List<?> others = entityStore.listColonies(system);

		doMigration(unemployed, samePlanet, MIGRATION_DISTANCE_SAME_PLANET);
		doMigration(unemployed, sameLocation, MIGRATION_DISTANCE_SAME_LOCATION);
		doMigration(unemployed, sameSystem, MIGRATION_DISTANCE_SAME_SYSTEM);
		doMigration(unemployed, others, MIGRATION_DISTANCE_OTHER_SYSTEM);
	}

	private void doMigration(Unemployed unemployed, Colony colony,
			double distanceModifier) {
		if (unemployed.getQuantity() < 1)
			return;
		List<?> colonists = entityStore.listColonists(colony, unemployed
				.getPopClass());
		double happiness = AColonists.getAverageHappiness(colonists);

		double migrationRate = unemployed.getHappiness() - happiness;
		migrationRate *= distanceModifier;

		int migrate = (int) (unemployed.getQuantity() * migrationRate);

		if (migrate > 0) {
			unemployed.removeCash(migrate * unemployed.getCashPerPerson());
			unemployed.removePopulation(migrate);
			Unemployed other = entityStore.getUnemployed(colony, unemployed
					.getPopClass());
			if (other == null) {
				other = new Unemployed();
				other.setCash(0);
				other.setColony(colony);
				other.setHappiness(0.0);
				other.setPopulation(new Population(unemployed.getPopClass()));
				entityStore.save(other);
			}
			other.addPopulation(migrate);
			if (log.isDebugEnabled())
				log.debug(migrate + " of " + unemployed.getPopClass()
						+ " migrated to " + other);
		}
	}

	private void doMigration(Unemployed unemployed, List<?> colonies,
			double distanceModifier) {
		if (unemployed.getQuantity() < 1)
			return;
		Iterator<?> i = colonies.iterator();
		while (i.hasNext()) {
			doMigration(unemployed, (Colony) i.next(), distanceModifier);
		}
	}

	private void doServiceNeeds(AColonists colonists) {
		int quality = colonists.getPopClass().getServiceQualityRequired();
		List<AFacilityType> medicalTypes = ServiceFacility.listMedical(quality);
		doServiceNeeds(colonists, medicalTypes, HAPPINESS_MEDICAL);
		List<AFacilityType> fitnessTypes = ServiceFacility.listFitness(quality);
		doServiceNeeds(colonists, fitnessTypes, HAPPINESS_FITNESS);
		List<AFacilityType> entTypes = ServiceFacility
				.listEntertainment(quality);
		doServiceNeeds(colonists, entTypes, HAPPINESS_ENTERTAINMENT);
		List<AFacilityType> eduTypes = ServiceFacility.listEducation(quality);
		doServiceNeeds(colonists, eduTypes, HAPPINESS_EDUCATION);
	}

	private void doServiceNeeds(AColonists colonists,
			List<AFacilityType> types, double happinessRating) {
		Colony colony = colonists.getColony();
		Map<Facility, List<?>> facilities = entityStore
				.listFacilitiesWithWorkers(colony, types);
		Facility.ServiceResult result = Facility.service(ServerConfiguration
				.getCurrentDate(), facilities, colonists.getQuantity(),
				colonists.getCash());

		double ratio = (double) colonists.getQuantity()
				/ (double) result.quantityServiced;
		double happiness = ratio * happinessRating;

		colonists.removeCash(result.totalCost);
		colonists.addHappiness(happiness);

		if (log.isDebugEnabled())
			log.debug(colonists + " used " + result.quantityServiced
					+ " services");
	}

	private void hireWorkers(Workers workers) {
		Facility facility = workers.getFacility();
		PopulationClass popClass = workers.getPopClass();
		Population required = facility.getTypeClass().getWorkerRequirement(
				popClass);
		if (workers == null
				|| workers.getPopulation().getQuantity() < required
						.getQuantity()) {
			if (log.isDebugEnabled())
				log.debug("Hiring workers for " + facility + " of " + popClass);
			// more needed so hire any unemployed
			Unemployed unemployed = entityStore.getUnemployed(facility
					.getColony(), popClass);
			if (unemployed != null) {
				int qty = required.getQuantity()
						- workers.getPopulation().getQuantity();
				if (qty < unemployed.getQuantity()) {
					qty = unemployed.getQuantity();
				}
				if (qty > 0) {
					if (workers == null) {
						workers = new Workers();
						workers.setPopulation(new Population());
						workers.getPopulation().setPopClass(popClass);
						workers.setColony(facility.getColony());
						workers.setFacility(facility);
						workers.setSalary(0); // we set salary to zero as the
												// owner hasn't specified one
						entityStore.save(workers);
					}
					int cash = qty * unemployed.getCashPerPerson();
					workers.addPopulation(qty);
					unemployed.removePopulation(qty);
					workers.addCash(cash);
					unemployed.removeCash(cash);
					if (log.isDebugEnabled())
						log.debug(facility + " hired " + qty + " of "
								+ popClass);
				}
			}
		}
	}

	private void payGrants(AColonists colonists) {
		ColonistGrant grant = entityStore.getColonistGrant(colonists
				.getColony(), colonists.getPopulation().getPopClass(), true);
		if (grant != null) {
			Corporation govt = grant.getColony().getGovernment();
			int cash = grant.getCredits() * colonists.getQuantity();
			if (cash < govt.getCredits()) {
				cash = govt.getCredits();
			}
			Object[] args = { colonists.getPopClass().getName(),
					String.valueOf(colonists.getQuantity()) };
			String desc = CashTransaction.getDescription(
					CashTransaction.GRANT_PAID, args);
			govt.add(cash, ServerConfiguration.getCurrentDate(), desc);
			colonists.addCash(cash);
			if (log.isDebugEnabled())
				log.debug(colonists + " paid grant of " + cash);
		}
	}

	private void payWorkers(Workers workers) {
		Corporation employer = workers.getFacility().getOwner();
		int salaryAvail = employer.getCredits() / workers.getSalary();
		int qty = workers.getPopulation().getQuantity();
		if (salaryAvail < qty) {
			int quit = qty - salaryAvail;
			int redundancy = (quit * workers.getSalary());
			Unemployed unemployed = entityStore.getUnemployed(workers
					.getColony(), workers.getPopulation().getPopClass());
			if (unemployed == null) {
				unemployed = new Unemployed();
				unemployed.setPopulation(new Population(workers.getPopClass()));
				unemployed.setColony(workers.getColony());
				entityStore.save(unemployed);
			}
			unemployed.addPopulation(quit);
			workers.removePopulation(quit);
			if (log.isDebugEnabled())
				log.debug(workers.getFacility() + " had " + quit + " x "
						+ workers.getPopClass()
						+ " quitters due to unpaid salary.");
			Object[] args = { workers.getPopulation().getPopClass().getName(),
					String.valueOf(quit) };
			String desc = CashTransaction.getDescription(
					CashTransaction.REDUNDANCY_PAY, args);
			employer.remove(redundancy, ServerConfiguration.getCurrentDate(),
					desc);
			unemployed.addCash(redundancy);
			qty -= quit;
		}
		int salaryPaid = (qty * workers.getSalary());
		Object[] args = { workers.getPopulation().getPopClass().getName(),
				String.valueOf(qty) };
		String desc = CashTransaction.getDescription(
				CashTransaction.SALARY_PAID, args);
		employer.remove(salaryPaid, ServerConfiguration.getCurrentDate(), desc);
		workers.addCash(salaryPaid);
		if (log.isDebugEnabled())
			log.debug(workers + " paid " + salaryPaid);
	}

	public void process() {
		log.info("Starting population processor");
		List<?> colonists = entityStore.listColonists();
		int size = colonists.size();
		int i = 1;
		log.info(size + " colonists to process...");
		for (Object o : colonists) {
			if (log.isDebugEnabled())
				log.debug(i + " of " + size + ": Processing " + o);
			AColonists colonist = (AColonists) o;
			if (colonist instanceof Workers) {
				Workers workers = (Workers) colonist;
				hireWorkers(workers);
				payWorkers(workers);
			}
			payGrants(colonist);
			doConsumerNeeds(colonist);
			doServiceNeeds(colonist);

			if (colonist instanceof Unemployed) {
				Unemployed unemployed = (Unemployed) colonist;
				doMigration(unemployed);
			}
			if (colonist instanceof Workers) {
				Workers workers = (Workers) colonist;
				quitWorkers(workers);
				if (workers.getQuantity() < 1) {
					return;
				}
			}
			doBirths(colonist);
			doDeaths(colonist);
			i++;
		}
		log.info("Finished population processor");
	}

	private void quitWorkers(Workers workers) {
		Facility facility = workers.getFacility();
		double quitChance = 100.0 - workers.getHappiness();
		int total = workers.getPopulation().getQuantity();
		int quitters = (int) (total * (quitChance / 100.0));
		if (log.isDebugEnabled())
			log.debug(facility + " had " + quitters + " x "
					+ workers.getPopClass() + " quitters. Chance: "
					+ quitChance);
		if (quitters > 0) {
			Unemployed unemployed = entityStore.getUnemployed(facility
					.getColony(), workers.getPopClass());
			if (unemployed == null) {
				unemployed = new Unemployed();
				unemployed.setColony(facility.getColony());
				unemployed.setPopulation(new Population(workers.getPopClass()));
				entityStore.save(unemployed);
			}
			int cash = quitters * workers.getCashPerPerson();
			workers.removePopulation(quitters);
			unemployed.addPopulation(quitters);
			if (cash > 0) {
				workers.removeCash(cash);
				unemployed.addCash(cash);
			}
		}
	}

}
