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
import starcorp.common.entities.ColonistGrant;
import starcorp.common.entities.Colony;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.Facility;
import starcorp.common.entities.MarketItem;
import starcorp.common.entities.Unemployed;
import starcorp.common.entities.Workers;
import starcorp.common.types.AFacilityType;
import starcorp.common.types.AItemType;
import starcorp.common.types.CashTransaction;
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
	private static final int MAX_BIRTH_PERCENT = 10;

	private static final Random rnd = new Random(System.currentTimeMillis());
	private static final Log log = LogFactory.getLog(ColonyUpdater.class);
	
	private Colony colony;
	private Map<AItemType, List<MarketItem>> market = new HashMap<AItemType, List<MarketItem>>();
	private Map<AFacilityType, Map<Facility, List<Workers>>> services = new HashMap<AFacilityType, Map<Facility, List<Workers>>>();
	private Map<PopulationClass, ColonistGrant> grants = new HashMap<PopulationClass, ColonistGrant>();
	private List<AColonists> colonists = new ArrayList<AColonists>();
	
	private PopulationProcessor processor;
	
	public ColonyUpdater(Colony colony,
			Map<AItemType, List<MarketItem>> market,
			Map<AFacilityType, Map<Facility, List<Workers>>> services,
			Map<PopulationClass, ColonistGrant> grants,
			List<AColonists> colonists, PopulationProcessor processor) {
		super();
		this.colony = colony;
		this.market = market;
		this.services = services;
		this.grants = grants;
		this.colonists = colonists;
		this.processor = processor;
	}

	/* (non-Javadoc)
	 * @see starcorp.server.engine.AServerTask#doJob()
	 */
	@Override
	protected void doJob() throws Exception {
		int size = colonists.size();
		log.info(this + ": " + size + " colonists to update for " + colony);
		int i = 1;
		for(Object o : colonists) {
			if(log.isDebugEnabled())
				log.debug(this + ": " + i + " of " + size + " processing.");
			AColonists pop = (AColonists) o;
			if (pop instanceof Workers) {
				Workers workers = (Workers) pop;
				hireWorkers(workers);
				payWorkers(workers);
			}
			payGrants(pop);
			doConsumerNeeds(pop);
			doServiceNeeds(pop);
			if (pop instanceof Workers) {
				Workers workers = (Workers) pop;
				quitWorkers(workers);
			}
			doBirths(pop);
			doDeaths(pop);
		}
		processor.done(this);
	}
	
	private void payGrants(AColonists colonists) {
		ColonistGrant grant = grants.get(colonists.getPopClass());
		if (grant != null) {
			Corporation govt = grant.getColony().getGovernment();
			int cash = grant.getCredits() * colonists.getQuantity();
			int avail = entityStore.getCredits(govt);
			if (cash < avail) {
				cash = avail;
			}
			Object[] args = { colonists.getPopClass().getName(),
					String.valueOf(colonists.getQuantity()) };
			String desc = CashTransaction.getDescription(
					CashTransaction.GRANT_PAID, args);
			entityStore.transferCredits(govt, colonists, cash, desc);
			if (log.isDebugEnabled())
				log.debug(this + ": " + colonists + " paid grant of " + cash);
		}
	}
	
	private Unemployed getUnemployed(PopulationClass popClass) {
		Unemployed unemployed = entityStore.getUnemployed(colony, popClass);
		if(unemployed == null) {
			unemployed = new Unemployed();
			unemployed.setPopulation(new Population(popClass));
			unemployed.setColony(colony);
			entityStore.save(unemployed);
		}
		return unemployed;
	}

	private void payWorkers(Workers workers) {
		beginTransaction();
		Corporation employer = workers.getFacility().getOwner();
		int salaryAvail = entityStore.getCredits(employer) / workers.getSalary();
		int qty = workers.getPopulation().getQuantity();
		if (salaryAvail < qty) {
			int quit = qty - salaryAvail;
			int redundancy = (quit * workers.getSalary());
			Unemployed unemployed = getUnemployed(workers.getPopClass());
			unemployed.addPopulation(quit);
			workers.removePopulation(quit);
			entityStore.save(unemployed);
			entityStore.save(workers);
			commit();
			if (log.isDebugEnabled())
				log.debug(this + ": " + workers.getFacility() + " had " + quit + " x "
						+ workers.getPopClass()
						+ " quitters due to unpaid salary.");
			Object[] args = { workers.getPopulation().getPopClass().getName(),
					String.valueOf(quit) };
			String desc = CashTransaction.getDescription(
					CashTransaction.REDUNDANCY_PAY, args);
			entityStore.transferCredits(employer, unemployed, redundancy, desc);
			qty -= quit;
		}
		int salaryPaid = (qty * workers.getSalary());
		Object[] args = { workers.getPopulation().getPopClass().getName(),
				String.valueOf(qty) };
		String desc = CashTransaction.getDescription(
				CashTransaction.SALARY_PAID, args);
		entityStore.transferCredits(employer, workers, salaryPaid, desc);
		if (log.isDebugEnabled())
			log.debug(this + ": " + workers + " paid " + salaryPaid);
	}

	private void quitWorkers(Workers workers) {
		beginTransaction();
		Facility facility = workers.getFacility();
		double quitChance = 100.0 - workers.getHappiness();
		if(quitChance < 0.0)
			quitChance = 0.0;
		int total = workers.getPopulation().getQuantity();
		int quitters = (int) (total * (quitChance / 100.0));
		if (log.isDebugEnabled())
			log.debug(this + ": " + facility + " had " + quitters + " x "
					+ workers.getPopClass() + " quitters. Chance: "
					+ quitChance);
		if (quitters > 0) {
			Unemployed unemployed = getUnemployed(workers.getPopClass());
			int cashPerPerson = entityStore.getCredits(workers) / workers.getQuantity();
			int cash = quitters * cashPerPerson;
			workers.removePopulation(quitters);
			unemployed.addPopulation(quitters);
			entityStore.save(workers);
			entityStore.save(unemployed);
			commit();
			if (cash > 0) {
				entityStore.transferCredits(workers, unemployed, cash, "");
			}
		}
	}

	private void hireWorkers(Workers workers) {
		beginTransaction();
		Facility facility = workers.getFacility();
		PopulationClass popClass = workers.getPopClass();
		Population required = facility.getTypeClass().getWorkerRequirement(
				popClass);
		if (workers.getPopulation().getQuantity() < required
						.getQuantity()) {
			if (log.isDebugEnabled())
				log.debug(this + ": Hiring workers for " + facility + " of " + popClass);
			// more needed so hire any unemployed
			Unemployed unemployed = getUnemployed(workers.getPopClass());
			int qty = required.getQuantity()
					- workers.getPopulation().getQuantity();
			if (qty < unemployed.getQuantity()) {
				qty = unemployed.getQuantity();
			}
			if (qty > 0) {
				int cashPerPerson = entityStore.getCredits(unemployed) / unemployed.getQuantity();
				int cash = qty * cashPerPerson;
				workers.addPopulation(qty);
				unemployed.removePopulation(qty);
				entityStore.save(workers);
				entityStore.save(unemployed);
				commit();
				entityStore.transferCredits(unemployed, workers, cash, "");
				if (log.isDebugEnabled())
					log.debug(this + ": " + facility + " hired " + qty + " of "
							+ popClass);
			
			}
		}
	}
	
	private Map<Facility, List<Workers>> getFacilities(List<AFacilityType> types) {
		HashMap<Facility,List<Workers>> map = new HashMap<Facility, List<Workers>>();
		for(AFacilityType type : types) {
			Map<Facility, List<Workers>> m = services.get(type);
			if(m != null)
				map.putAll(m);
		}
		return map;
	}

	private void doServiceNeeds(AColonists colonists,
			List<AFacilityType> types, double happinessRating) {
		beginTransaction();
		Facility.ServiceResult result = Util.service(ServerConfiguration
				.getCurrentDate(), getFacilities(types), colonists.getQuantity(),
				entityStore.getCredits(colonists),entityStore);
		commit();
		beginTransaction();
		double ratio = (double) colonists.getQuantity()
				/ (double) result.quantityServiced;
		double happiness = ratio * happinessRating;
		colonists.addHappiness(happiness);
		entityStore.save(colonists);
		commit();
		entityStore.removeCredits(colonists, result.totalCost, "");
		if (log.isDebugEnabled())
			log.debug(this + ": " + colonists + " used " + result.quantityServiced
					+ " services");
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

	private void doDeaths(AColonists colonist) {
		beginTransaction();
		double deathRate = colonist.getColony().getHazardLevel();
		int deaths = (int) (colonist.getQuantity() * deathRate);
		if (deaths > 0) {
			colonist.removePopulation(deaths);
			entityStore.save(colonist);
			if (log.isDebugEnabled())
				log.debug(this + ": " + deaths + " of " + colonist.getPopClass() + " died.");
		}
		commit();

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

	private void doConsumerNeeds(AColonists colonists, List<AItemType> types,
			double happinessRating) {
		beginTransaction();
		MarketItem.BuyResult result = Util.buy(ServerConfiguration
				.getCurrentDate(), getMarketItems(types), colonists.getQuantity(), 
				entityStore.getCredits(colonists),entityStore);
		commit();
		beginTransaction();
		double ratio = (double) colonists.getQuantity()
				/ (double) result.quantityBought;
		double happiness = ratio * happinessRating;
		colonists.addHappiness(happiness);
		entityStore.save(colonists);
		commit();
		entityStore.removeCredits(colonists, result.totalPrice, "");
		if (log.isDebugEnabled())
			log.debug(this + ": " + colonists + " bought " + result.quantityBought
					+ " consumer goods");
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

	private void doBirths(AColonists colonist) {
		beginTransaction();
		double birthRate = rnd.nextInt(MAX_BIRTH_PERCENT + 1) / 100.0;
		int births = (int) (colonist.getQuantity() * birthRate);
		if (births > 0) {
			Unemployed unemployed = getUnemployed(colonist.getPopClass());
			unemployed.addPopulation(births);
			entityStore.save(unemployed);
			if (log.isDebugEnabled())
				log.debug(this + ": " + births + " of " + colonist.getPopClass() + " born.");
		}
		commit();

	}

	/* (non-Javadoc)
	 * @see starcorp.server.engine.AServerTask#getLog()
	 */
	@Override
	protected Log getLog() {
		return log;
	}

	@Override
	protected void onException(Exception e) {
		super.onException(e);
		// retry
		engine.schedule(this);
	}

}
