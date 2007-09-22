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
package starcorp.server.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import starcorp.common.entities.AColonists;
import starcorp.common.entities.Colony;
import starcorp.common.entities.ColonyItem;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.Facility;
import starcorp.common.entities.MarketItem;
import starcorp.common.entities.Planet;
import starcorp.common.entities.Workers;
import starcorp.common.types.AFacilityType;
import starcorp.common.types.AItemType;
import starcorp.common.types.ConsumerGoods;
import starcorp.common.types.Coordinates2D;
import starcorp.common.types.Items;
import starcorp.common.types.PlanetMapSquare;
import starcorp.common.types.Population;
import starcorp.common.types.PopulationClass;
import starcorp.common.types.ServiceFacility;
import starcorp.common.util.PackageExplorer;
import starcorp.server.ServerConfiguration;
import starcorp.server.entitystore.IEntityStore;

/**
 * starcorp.server.setup.AColonyTemplate
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 20 Sep 2007
 */
public abstract class AColonyTemplate {

	private static Map<String, AColonyTemplate> templates = new HashMap<String, AColonyTemplate>();
	
	static {
		List<Class> classes;
		try {
			classes = PackageExplorer.getClassesForPackage("starcorp.server.setup.colonies");
			for(Class clazz : classes) {
				AColonyTemplate template = (AColonyTemplate) clazz.newInstance();
				templates.put(clazz.getSimpleName(), template);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static AColonyTemplate getTemplate(String name) {
		return templates.get(name);
	}
	
	protected Log log = LogFactory.getLog(AColonyTemplate.class);
	
	protected IEntityStore entityStore;
	
	public void setEntityStore(IEntityStore entityStore) {
		this.entityStore = entityStore;
	}
	
	public Colony create(int govtID, int planetID, int x, int y, String name) {
		Corporation govt = (Corporation) entityStore.load(Corporation.class, govtID);
		Planet planet = (Planet) entityStore.load(Planet.class, planetID);
		if(govt == null || planet == null) {
			return null;
		}
		Coordinates2D location = new Coordinates2D(x,y);
		return create(govt,planet,location,name);
	}
	
	public Colony create(Corporation govt, Planet planet, Coordinates2D location, String name) {
		PlanetMapSquare sq = planet.get(location);
		if(sq == null) {
			System.out.println("Invalid location");
			return null;
		}
		if(entityStore.getColony(planet, location) != null) {
			System.out.println("Colony already exists at location!");
			return null;
		}
		Colony colony = new Colony();
		colony.setFoundedDate(ServerConfiguration.getCurrentDate());
		colony.setGovernment(govt);
		colony.setHazardLevel(planet.getAtmosphereTypeClass().getHazardLevel() + sq.getTerrainType().getHazardLevel());
		colony.setLocation(location);
		colony.setName(name);
		colony.setPlanet(planet);
		entityStore.save(colony);
		log.info("Created " + colony);
		List<Facility> facilities = createFacilities(colony);
		List<AColonists> population = populate(colony, facilities);
		createServiceFacilities(colony, facilities, population);
		createConsumerGoods(colony, population);
		return colony;
	}
	
	protected void createConsumerGoods(Colony colony, List<AColonists> population) {
		Map<PopulationClass,Integer> count = count(population);
		for(PopulationClass popClass : count.keySet()) {
			int n = count.get(popClass) * 100;
			int quality = popClass.getConsumerQualityRequired();
			int charge = popClass.getNPCSalary() / 8;
			AItemType food = ConsumerGoods.listFood(quality).get(0);
			AItemType drink = ConsumerGoods.listDrink(quality).get(0);
			AItemType intoxicant = ConsumerGoods.listIntoxicant(quality).get(0);
			AItemType clothes = ConsumerGoods.listClothes(quality).get(0);
			
			createItem(colony, food, n, charge);
			createItem(colony, drink, n, charge);
			createItem(colony, intoxicant, n, charge);
			createItem(colony, clothes, n, charge);
		}
	}
	
	protected void createPower(Colony colony, List<Facility> facilities) {
		int required = 0;
		for(Facility facility : facilities) {
			AFacilityType type = facility.getTypeClass();
			required += type.getPowerRequirement();
		}
		required *= 100;
		addItem(colony,"nuclear-power",required);
	}
	
	protected MarketItem createItem(Colony colony, AItemType type, int quantity, int price) {
		MarketItem item = new MarketItem();
		item.setColony(colony);
		item.setCostPerItem(price);
		item.setItem(new Items(type,quantity));
		item.setSeller(colony.getGovernment());
		entityStore.save(item);
		if(log.isDebugEnabled())log.debug("Created " + item);
		return item;
	}
	
	protected ColonyItem addItem(Colony colony, String type, int quantity) {
		ColonyItem item = entityStore.getItem(colony, colony.getGovernment(), AItemType.getType(type)); 
		if(item == null) {
			item = new ColonyItem();
			item.setColony(colony);
			item.setItem(new Items());
			item.getItem().setType(type);
			item.setOwner(colony.getGovernment());
		}
		item.add(quantity);
		entityStore.save(item);
		if(log.isDebugEnabled())log.debug("Created " + item);
		return item;
	}
	
	protected Map<PopulationClass,Integer> count(List<AColonists> population) {
		Map<PopulationClass,Integer> count = new HashMap<PopulationClass, Integer>();
		for(AColonists colonist : population) {
			PopulationClass popClass = colonist.getPopClass();
			int popCount = count.containsKey(popClass) ? count.get(popClass) : 0;
			popCount += colonist.getQuantity();
			count.put(colonist.getPopClass(), popCount);
		}
		return count;
	}
	
	protected void createServiceFacilities(Colony colony, List<Facility> facilities, List<AColonists> population) {
		Map<PopulationClass,Integer> count = count(population);
		Map<AFacilityType, Integer> required = new HashMap<AFacilityType, Integer>();
		for(PopulationClass popClass : count.keySet()) {
			int n = count.get(popClass);
			int quality = popClass.getServiceQualityRequired();
			AFacilityType medical = ServiceFacility.listMedical(quality).get(0);
			AFacilityType fitness = ServiceFacility.listFitness(quality).get(0);
			AFacilityType ent = ServiceFacility.listEntertainment(quality).get(0);
			AFacilityType edu = ServiceFacility.listEducation(quality).get(0);
			int total = 0;
			if(required.containsKey(medical)) {
				total = required.get(medical);
			}
			total += n;
			required.put(medical,total);
			total = 0;
			if(required.containsKey(fitness)) {
				total = required.get(fitness);
			}
			total += n;
			required.put(fitness,total);
			total = 0;
			if(required.containsKey(ent)) {
				total = required.get(ent);
			}
			total += n;
			required.put(ent,total);
			total = 0;
			if(required.containsKey(edu)) {
				total = required.get(edu);
			}
			total += n;
			required.put(edu,total);
		}
		for(AFacilityType type : required.keySet()) {
			int totalPop = required.get(type);
			int total = totalPop / ServiceFacility.MAX_COLONISTS_SERVICED;
			if(totalPop % ServiceFacility.MAX_COLONISTS_SERVICED > 0)
				total++;
			for(int i = 0; i <total; i++) {
				Facility f = createFacility(colony, type.getKey());
				populate(f, population);
				f.setServiceCharge(100 * ((ServiceFacility)type).getQuality());
				entityStore.save(f);
			}
		}
	}
	
	protected Workers createWorker(Facility facility, Population pop) {
		Workers worker = new Workers();
		worker.setColony(facility.getColony());
		worker.setFacility(facility);
		worker.setSalary(pop.getPopClass().getNPCSalary());
		worker.setPopulation(pop);
		entityStore.save(worker);
		if(log.isDebugEnabled())log.debug("Created " + worker);
		return worker;
	}
	
	protected void populate(Facility facility, List<AColonists> colonists) {
		for(PopulationClass popClass : facility.getTypeClass().getWorkerRequirement().keySet()) {
			AFacilityType facType = facility.getTypeClass();
			Population pop = facType.getWorkerRequirement(popClass);
			colonists.add(createWorker(facility, pop));
		}
		
	}
	
	protected List<AColonists> populate(Colony colony, List<Facility> facilities) {
		List<AColonists> colonists = new ArrayList<AColonists>();
		for(Facility facility : facilities) {
			populate(facility, colonists);
		}
		return colonists;
	}
	
	protected List<Facility> createFacilities(Colony colony) {
		List<Facility> facilities = new ArrayList<Facility>();
		int farms = countFarms();
		int pumps = countPumps();
		int mines = countMines();
		int refineries = countRefineries();
		int lfact = countLightFactories();
		int hfact = countHeavyFactories();
		int shfact = countSuperHeavyFactories();
		int shipyards = countShipyards();
		facilities.add(createFacility(colony,"hub"));
		for(int i = 0; i < farms; i++) {
			facilities.add(createFacility(colony, "farm"));
		}
		for(int i = 0; i < mines; i++) {
			facilities.add(createFacility(colony, "mine"));
		}
		for(int i = 0; i < pumps; i++) {
			facilities.add(createFacility(colony, "pump"));
		}
		for(int i = 0; i < refineries; i++) {
			facilities.add(createFacility(colony, "refinery"));
		}
		for(int i = 0; i < lfact; i++) {
			facilities.add(createFacility(colony, "light-factory"));
		}
		for(int i = 0; i < hfact; i++) {
			facilities.add(createFacility(colony, "heavy-factory"));
		}
		for(int i = 0; i < shfact; i++) {
			facilities.add(createFacility(colony, "super-heavy-factory"));
		}
		for(int i = 0; i < shipyards; i++) {
			facilities.add(createFacility(colony, "shipyard"));
		}
		if(hasOrbitalDock()) {
			facilities.add(createFacility(colony, "dock"));
		}
		return facilities;
	}
	
	protected Facility createFacility(Colony colony, String type) {
		Facility facility = new Facility();
		facility.setBuiltDate(ServerConfiguration.getCurrentDate());
		facility.setColony(colony);
		facility.setOpen(true);
		facility.setOwner(colony.getGovernment());
		facility.setType(type);
		entityStore.save(facility);
		if(log.isDebugEnabled())log.debug("Created " + facility);
		return facility;
	}
	
	protected abstract int countFarms();
	protected abstract int countMines();
	protected abstract int countPumps();
	protected abstract int countRefineries();
	protected abstract int countPlants();
	protected abstract int countLightFactories();
	protected abstract int countHeavyFactories();
	protected abstract int countSuperHeavyFactories();
	protected abstract int countShipyards();
	protected abstract boolean hasOrbitalDock();
}
