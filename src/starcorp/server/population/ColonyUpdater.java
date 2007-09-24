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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import starcorp.common.entities.AColonists;
import starcorp.common.entities.CashTransaction;
import starcorp.common.entities.ColonistGrant;
import starcorp.common.entities.Colony;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.Facility;
import starcorp.common.entities.MarketItem;
import starcorp.common.entities.Unemployed;
import starcorp.common.entities.Workers;
import starcorp.common.types.AFacilityType;
import starcorp.common.types.AItemType;
import starcorp.common.types.ConsumerGoods;
import starcorp.common.types.Population;
import starcorp.common.types.PopulationClass;
import starcorp.common.types.ServiceFacility;
import starcorp.server.ServerConfiguration;
import starcorp.server.Util;
import starcorp.server.engine.AServerTask;

/**
 * starcorp.server.population.ColonyUpdater
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 22 Sep 2007
 */
public class ColonyUpdater extends AServerTask {
	private static final double HAPPINESS_CLOTHES = 15.0;

	private static final double HAPPINESS_DRINK = 10.0;
	private static final double HAPPINESS_EDUCATION = 15.0;
	private static final double HAPPINESS_ENTERTAINMENT = 15.0;
	private static final double HAPPINESS_FITNESS = 10.0;
	private static final double HAPPINESS_FOOD = 10.0;
	private static final double HAPPINESS_INTOXICANT = 15.0;
	private static final double HAPPINESS_MEDICAL = 10.0;
	private static final Log log = LogFactory.getLog(ColonyUpdater.class);

	private static final int MAX_BIRTH_PERCENT = 10;
	private static final Random rnd = new Random(System.currentTimeMillis());
	
	private final Colony colony;
	private Map<PopulationClass, ColonistGrant> grants = new HashMap<PopulationClass, ColonistGrant>();
	private Map<AItemType, List<MarketItem>> market = new HashMap<AItemType, List<MarketItem>>();
	private final PopulationProcessor processor;
	private Map<AFacilityType, Map<Facility, List<AColonists>>> services = new HashMap<AFacilityType, Map<Facility, List<AColonists>>>();
	
	public ColonyUpdater(PopulationProcessor processor, Colony colony) {
		this.colony = colony;
		this.processor = processor;
	}
	
	private void doBirths(AColonists colonist) {
		double birthRate = rnd.nextInt(MAX_BIRTH_PERCENT + 1) / 100.0;
		int qty = colonist.getQuantity();
		int births = (int) (qty * birthRate);
		if (births > 0) {
			Unemployed unemployed = getUnemployed(colonist.getPopClass());
			unemployed.addPopulation(births);
			entityStore.update(unemployed);
			if (log.isDebugEnabled())
				log.debug(this + ": " + births + " of " + colonist.getPopClass() + " born.");
		}

	}
	
	private void doBirths(List<AColonists> colonists) {
		for(AColonists col : colonists) {
			doBirths(col);
		}
	}
	
	private void doNeeds() {
		List<PopulationClass> types = PopulationClass.listTypes();
		for(PopulationClass type : types) {
			doConsumerNeeds(type);
			doServiceNeeds(type);
		}
	}
	
	private void doConsumerNeeds(AColonists colonists, List<AItemType> types,
			double happinessRating) {
		int qty = colonists.getQuantity();
		MarketItem.BuyResult result = Util.buy(ServerConfiguration
				.getCurrentDate(), getMarketItems(types), qty, 
				entityStore.getCredits(colonists),entityStore);
		double ratio = qty > 0 ?
				(double) result.quantityBought / (double) qty
				: 0.0;
		double happiness = ratio * happinessRating;
		colonists.addHappiness(happiness);
		entityStore.update(colonists);
		entityStore.removeCredits(colonists, result.totalPrice, "");
		if (log.isDebugEnabled())
			log.debug(this + ": " + colonists + " bought " + result.quantityBought
					+ " consumer goods");
	}
	
	private void doConsumerNeeds(List<AColonists> colonists, List<AItemType> types,double happiness) {
		if(log.isDebugEnabled())
			log.debug("Doing consumer needs for " + colonists.size() + " using " + types + " giving " + happiness + " happiness");
		for(AColonists col : colonists) {
			doConsumerNeeds(col,types,happiness);
		}
	}

	private void doConsumerNeeds(PopulationClass popClass) {
		int quality = popClass.getConsumerQualityRequired();
		List<AItemType> foodTypes = ConsumerGoods.listFood(quality);
		doConsumerNeeds(entityStore.listColonists(colony, popClass), foodTypes, HAPPINESS_FOOD);
		List<AItemType> drinkTypes = ConsumerGoods.listDrink(quality);
		doConsumerNeeds(entityStore.listColonists(colony, popClass), drinkTypes, HAPPINESS_DRINK);
		List<AItemType> intoxicantTypes = ConsumerGoods.listIntoxicant(quality);
		doConsumerNeeds(entityStore.listColonists(colony, popClass), intoxicantTypes, HAPPINESS_INTOXICANT);
		List<AItemType> clothesTypes = ConsumerGoods.listClothes(quality);
		doConsumerNeeds(entityStore.listColonists(colony, popClass), clothesTypes, HAPPINESS_CLOTHES);
	}
	
	private void doDeaths(AColonists colonist) {
		double deathRate = colonist.getColony().getHazardLevel();
		int qty = colonist.getQuantity();
		int deaths = (int) (qty * deathRate);
		if (deaths > 0) {
			colonist.removePopulation(deaths);
			entityStore.update(colonist);
			if (log.isDebugEnabled())
				log.debug(this + ": " + deaths + " of " + colonist.getPopClass() + " died.");
		}
	}

	private void doDeaths(List<AColonists> colonists) {
		for(AColonists col : colonists) {
			doDeaths(col);
		}
	}

	private void doServiceNeeds(PopulationClass popClass) {
		int quality = popClass.getServiceQualityRequired();
		List<AFacilityType> medicalTypes = ServiceFacility.listMedical(quality);
		doServiceNeeds(entityStore.listColonists(colony, popClass), medicalTypes, HAPPINESS_MEDICAL);
		List<AFacilityType> fitnessTypes = ServiceFacility.listFitness(quality);
		doServiceNeeds(entityStore.listColonists(colony, popClass), fitnessTypes, HAPPINESS_FITNESS);
		List<AFacilityType> entTypes = ServiceFacility
				.listEntertainment(quality);
		doServiceNeeds(entityStore.listColonists(colony, popClass), entTypes, HAPPINESS_ENTERTAINMENT);
		List<AFacilityType> eduTypes = ServiceFacility.listEducation(quality);
		doServiceNeeds(entityStore.listColonists(colony, popClass), eduTypes, HAPPINESS_EDUCATION);
	}
	private void doServiceNeeds(AColonists colonists,
			List<AFacilityType> types, double happinessRating) {
		int qty = colonists.getQuantity();
		Facility.ServiceResult result = Util.service(ServerConfiguration
				.getCurrentDate(), getFacilities(types), qty,
				entityStore.getCredits(colonists),entityStore);
		double ratio = qty > 0 ? 
					(double) result.quantityServiced / (double) qty 
					: 0.0;
		double happiness = ratio * happinessRating;
		colonists.addHappiness(happiness);
		entityStore.update(colonists);
		entityStore.removeCredits(colonists, result.totalCost, "");
		if (log.isDebugEnabled())
			log.debug(this + ": " + colonists + " used " + result.quantityServiced
					+ " services");
	}
	
	private void doServiceNeeds(List<AColonists> colonists, List<AFacilityType> types,double happiness) {
		if(log.isDebugEnabled())
			log.debug("Doing service needs for " + colonists.size() + " using " + types + " giving " + happiness + " happiness");
		for(AColonists col : colonists) {
			doServiceNeeds(col,types,happiness);
		}
	}

	private Map<Facility, List<AColonists>> getFacilities(List<AFacilityType> types) {
		HashMap<Facility,List<AColonists>> map = new HashMap<Facility, List<AColonists>>();
		for(AFacilityType type : types) {
			Map<Facility, List<AColonists>> m = services.get(type);
			if(m != null)
				map.putAll(m);
		}
		return map;
	}
	
	private List<MarketItem> getMarketItems(List<AItemType> types) {
		List<MarketItem> list = new ArrayList<MarketItem>();
		for(AItemType type : types) {
			List<MarketItem> list2 = market.get(type);
			if(list2 != null)
				list.addAll(list2);
		}
		return list;
	}

	private Unemployed getUnemployed(PopulationClass popClass) {
		Unemployed unemployed = entityStore.getUnemployed(colony, popClass);
		if(unemployed == null) {
			unemployed = new Unemployed();
			unemployed.setPopClass(popClass);
			unemployed.setColony(colony);
			entityStore.create(unemployed);
		}
		return unemployed;
	}
	
	private void hireWorkers(List<AColonists> workers) {
		for(AColonists col : workers) {
			hireWorkers((Workers)col);
		}
	}

	private void hireWorkers(Workers workers) {
		Facility facility = workers.getFacility();
		PopulationClass popClass = workers.getPopClass();
		Population required = facility.getTypeClass().getWorkerRequirement(
				popClass);
		int workerQuantity = workers.getQuantity();
		if (workerQuantity < required
						.getQuantity()) {
			if (log.isDebugEnabled())
				log.debug(this + ": Hiring workers for " + facility + " of " + popClass);
			// more needed so hire any unemployed
			Unemployed unemployed = getUnemployed(workers.getPopClass());
			int qty = required.getQuantity()
					- workerQuantity;
			if (qty < unemployed.getQuantity()) {
				qty = unemployed.getQuantity();
			}
			if (qty > 0) {
				int colonistCount = unemployed.getQuantity();
				long cashPerPerson = colonistCount == 0 ? 0 : entityStore.getCredits(unemployed) / colonistCount;
				long cash = qty * cashPerPerson;
				workers.addPopulation(qty);
				unemployed.removePopulation(qty);
				entityStore.update(workers);
				entityStore.update(unemployed);
				entityStore.transferCredits(unemployed, workers, cash, "");
				if (log.isDebugEnabled())
					log.debug(this + ": " + facility + " hired " + qty + " of "
							+ popClass);
			
			}
		}
	}
	
	private void payGrants(AColonists colonists) {
		ColonistGrant grant = grants.get(colonists.getPopClass());
		if (grant != null) {
			Corporation govt = grant.getColony().getGovernment();
			int qty = colonists.getQuantity();
			int cash = grant.getCredits() * qty;
			long avail = entityStore.getCredits(govt);
			if (cash < avail) {
				cash = (int) avail;
			}
			Object[] args = { colonists.getPopClass().getName(),
					String.valueOf(qty) };
			String desc = CashTransaction.getDescription(
					CashTransaction.GRANT_PAID, args);
			entityStore.transferCredits(govt, colonists, cash, desc);
			if (log.isDebugEnabled())
				log.debug(this + ": GRANTS : " + colonists + " paid grant of " + cash);
		}
	}

	private void payGrants(List<AColonists> colonists) {
		for(AColonists col : colonists) {
			payGrants(col);
		}
	}

	private void payWorkers(List<AColonists> workers) {
		for(AColonists col : workers) {
			payWorkers((Workers)col);
		}
	}
	
	private void payWorkers(Workers workers) {
		if(workers.getSalary() == 0)
			return;
		Corporation employer = workers.getFacility().getOwner();
		long credits = entityStore.getCredits(employer);
		long salaryAvail = credits / workers.getSalary();
		int qty = workers.getQuantity();
//		if(log.isDebugEnabled())
//			log.debug(employer + " has " + credits + " credits. Can pay " + salaryAvail + " of " + qty +" workers (" + workers.getSalary() + "ea.)");
		if (salaryAvail < qty) {
			int quit = (int) (qty - salaryAvail);
			Unemployed unemployed = getUnemployed(workers.getPopClass());
			unemployed.addPopulation(quit);
			workers.removePopulation(quit);
			entityStore.update(unemployed);
			entityStore.update(workers);
			if (log.isDebugEnabled())
				log.debug(this + ": PAYING : " + workers.getFacility() + " had " + quit + " x "
						+ workers.getPopClass()
						+ " quitters due to unpaid salary.");
			qty -= quit;
		}
		if(qty > 0) {
			int salaryPaid = (qty * workers.getSalary());
			Object[] args = { workers.getPopClass().getName(),
					String.valueOf(qty) };
			String desc = CashTransaction.getDescription(
					CashTransaction.SALARY_PAID, args);
			entityStore.transferCredits(employer, workers, salaryPaid, desc);
			if (log.isDebugEnabled())
				log.debug(this + ": PAYING : " + workers + " paid " + salaryPaid);
		}
	}
	
	private void quitWorkers(List<AColonists> workers) {
		for(AColonists col : workers) {
			quitWorkers((Workers)col);
		}
	}

	private void quitWorkers(Workers workers) {
		Facility facility = workers.getFacility();
		double quitChance = 100.0 - workers.getHappiness();
		if(quitChance < 0.0)
			quitChance = 0.0;
		int total = workers.getQuantity();
		int quitters = (int) (total * (quitChance / 100.0));
		if (log.isDebugEnabled())
			log.debug(this + ": " + facility + " had " + quitters + " x "
					+ workers.getPopClass() + " quitters. Chance: "
					+ quitChance);
		if (quitters > 0) {
			Unemployed unemployed = getUnemployed(workers.getPopClass());
			int colonistCount = unemployed.getQuantity();
			long cashPerPerson = colonistCount == 0 ? 0 : entityStore.getCredits(workers) / colonistCount;
			long cash = quitters * cashPerPerson;
			workers.removePopulation(quitters);
			unemployed.addPopulation(quitters);
			entityStore.update(workers);
			entityStore.update(unemployed);
			if (cash > 0) {
				entityStore.transferCredits(workers, unemployed, cash, "");
			}
		}
	}
	/* (non-Javadoc)
	 * @see starcorp.server.engine.AServerTask#doJob()
	 */
	@Override
	protected void doJob() throws Exception {
		market = 
			entityStore.mapMarketByItemType(colony, AItemType.listTypes(ConsumerGoods.class));
		services =
			entityStore.mapFacilitiesWithWorkersByType(colony, AFacilityType.listTypes(ServiceFacility.class));
			new HashMap<AFacilityType, Map<Facility, List<AColonists>>>();
		grants =
			entityStore.mapColonistGrantsByPopClass(colony, true);
		List<AColonists> colonists = entityStore.listWorkers(colony);
		int size = colonists.size();
		log.info(this + ": HIRING : " + size + " workers to check for hiring @ " + colony);
		hireWorkers(colonists);
		
		colonists = entityStore.listWorkers(colony);
		size = colonists.size();
		log.info(this + ": PAYING : " + size + " workers to pay @ " + colony);
		payWorkers(colonists);
		
		colonists = entityStore.listColonists(colony);
		size = colonists.size();
		log.info(this + ": GRANTS : " + size + " colonists to pay grants @ " + colony);
		payGrants(colonists);
		
		doNeeds();
		
		colonists = entityStore.listWorkers(colony);
		size = colonists.size();
		log.info(this + ": QUIT : " + size + " workers to check for quitting @ " + colony);
		quitWorkers(colonists);
		
		colonists = entityStore.listColonists(colony);
		size = colonists.size();
		log.info(this + ": BIRTHS : " + size + " colonists to check for births @ " + colony);
		doBirths(colonists);

		colonists = entityStore.listColonists(colony);
		size = colonists.size();
		log.info(this + ": DEATHS : " + size + " colonists to check for deaths @ " + colony);
		doDeaths(colonists);
		processor.done(this);
	}

	/* (non-Javadoc)
	 * @see starcorp.server.engine.AServerTask#getLog()
	 */
	@Override
	protected Log getLog() {
		return log;
	}

}
