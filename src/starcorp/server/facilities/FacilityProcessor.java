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
package starcorp.server.facilities;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import starcorp.common.entities.AColonists;
import starcorp.common.entities.Colony;
import starcorp.common.entities.ColonyItem;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.Facility;
import starcorp.common.entities.MarketItem;
import starcorp.common.entities.ResourceDeposit;
import starcorp.common.types.AFactoryItem;
import starcorp.common.types.AItemType;
import starcorp.common.types.Coordinates2D;
import starcorp.common.types.Factory;
import starcorp.common.types.IndustrialGoods;
import starcorp.common.types.Items;
import starcorp.common.types.ResourceGenerator;
import starcorp.server.ServerConfiguration;
import starcorp.server.Util;
import starcorp.server.engine.AServerTask;
import starcorp.server.entitystore.IEntityStore;

/**
 * starcorp.server.facilities.FacilityProcessor
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 18 Sep 2007
 */
public class FacilityProcessor extends AServerTask {

	private static final Log log = LogFactory.getLog(FacilityProcessor.class);
	
	private IEntityStore entityStore;
	
	public FacilityProcessor(IEntityStore store) {
		this.entityStore = store;
	}
	
	@Override
	protected void doJob() throws Exception {
		log.info(this + ": Started facility processor");
		List<Facility> facilities = entityStore.listFacilities();
		log.info(this + ": " + facilities.size() + " facilities to process");
		for(Facility facility : facilities) {
			List<AColonists> workers = entityStore.listWorkers(facility);
			process(facility,workers);
			Thread.yield();
		}
		log.info("Finished facility processor");
	}

	@Override
	protected Log getLog() {
		return log;
	}

	private void process(Facility facility, List<?> workers) {
		if(log.isDebugEnabled())
			log.debug("Processing " + facility);
		facility.setTransactionCount(0);
		facility.setPowered(false);
		usePower(facility);
		if(facility.getTypeClass() instanceof Factory) {
			processFactory(facility, workers);
		}
		else if(facility.getTypeClass() instanceof ResourceGenerator) {
			processGenerator(facility, workers);
		}
		entityStore.update(facility);
		if(log.isDebugEnabled())
			log.debug("Processed " + facility);
	}
	
	private void usePower(Facility facility) {
		int required = facility.getTypeClass().getPowerRequirement();
		if(log.isDebugEnabled())
			log.debug(facility + " requires " + required + " power");
		Corporation owner = facility.getOwner();
		Colony colony = facility.getColony();
		List<AItemType> types = IndustrialGoods.listPower();
		List<ColonyItem> items = entityStore.listItems(owner, colony, types);
		if(ColonyItem.count(items) < required) {
			// attempt to buy from market
			List<MarketItem> market = entityStore.listMarket(colony, types, 1);
			MarketItem.BuyResult result = Util.buy(ServerConfiguration.getCurrentDate(), market, required, entityStore.getCredits(owner),entityStore);
			if(result.quantityBought < required) {
				// not enough - just add this to the colonies stockpile
				Iterator<Items> i = result.bought.iterator();
				while(i.hasNext()) {
					Items item = i.next();
					ColonyItem cItem = entityStore.getItem(colony, owner, item.getTypeClass());
					if(cItem == null) {
						cItem = new ColonyItem();
						cItem.setColony(colony);
						cItem.setItem(new Items(item.getTypeClass()));
						cItem.setOwner(owner);
						entityStore.create(cItem);
					}
					cItem.add(item.getQuantity());
					entityStore.update(cItem);
				}
			}
			else {
				facility.setPowered(true);
			}
		}
		else {
			ColonyItem.use(items, required);
			facility.setPowered(true);
		}
		if(log.isDebugEnabled())
			log.debug(facility + (facility.isPowered() ? " is powered" : " isn't powered"));
	}
	
	private int hasMaterials(Colony colony, Corporation owner, AFactoryItem type) {
		int max = 0;
		Iterator<Items> i = type.getComponent().iterator();
		while(i.hasNext()) {
			Items item = i.next();
			ColonyItem cItem = entityStore.getItem(colony, owner, item.getTypeClass());
			int x = cItem.getQuantity() / item.getQuantity();
			if(x < max) {
				max = x;
			}
		}
		return max;
	}
	
	private void useMaterials(Colony colony, Corporation owner, AFactoryItem type, int quantity) {
		Iterator<Items> i = type.getComponent().iterator();
		while(i.hasNext()) {
			Items item = i.next();
			ColonyItem cItem = entityStore.getItem(colony, owner, item.getTypeClass());
			int qty = item.getQuantity() * quantity;
			cItem.remove(qty);
			entityStore.update(cItem);
		}
	}
	
	private Items build(Facility factory, Items item, int maxCapacity) {
		AFactoryItem type = (AFactoryItem) item.getTypeClass();
		int avail = maxCapacity - factory.getTransactionCount();
		int qty = avail / type.getMassUnits();
		if(qty > item.getQuantity()) {
			qty = item.getQuantity();
		}
		int materials = hasMaterials(factory.getColony(), factory.getOwner(), type);
		if(qty > materials) {
			qty = materials;
		}
		
		int used = qty * type.getMassUnits();
		factory.incTransactionCount(used);
		ColonyItem cItem = null;
		if(cItem == null) {
			cItem = new ColonyItem();
			cItem.setColony(factory.getColony());
			cItem.setItem(new Items(type));
			cItem.setOwner(factory.getOwner());
			entityStore.create(cItem);
		}
		cItem.add(qty);
		entityStore.update(cItem);
		if(log.isDebugEnabled())
			log.debug(factory +" produced " + qty + " x " + type);
		useMaterials(factory.getColony(),factory.getOwner(), type, qty); 
		return new Items(item.getTypeClass(),qty);
	}
	
	private void processFactory(Facility factory, List<?> workers) {
		if(log.isDebugEnabled())
			log.debug(factory + " beginning production.");
		factory.clearCreated();
		Factory type = (Factory) factory.getTypeClass();
		int maxCapacity = factory.isPowered() ? type.getCapacity(workers) : 0;
		int capacityUsed = factory.getTransactionCount();
		Iterator<Items> i = factory.queue();
		while(capacityUsed < maxCapacity && i.hasNext()) {
			Items item = build(factory, i.next(), maxCapacity);
			if(item != null) {
				factory.createdItem(item);
			}
			capacityUsed = factory.getTransactionCount();
		}
		if(log.isDebugEnabled())
			log.debug(factory + " production done.");
	}
	
	private void processGenerator(Facility generator, List<?> workers) {
		if(log.isDebugEnabled())
			log.debug(generator + " started generating.");
		Corporation owner = generator.getOwner();
		Colony colony = generator.getColony();
		Coordinates2D location = colony.getLocation();
		List<ResourceDeposit> deposits = entityStore.listDeposits(colony.getPlanetID(), location);
		double efficiency = generator.getEfficiency(workers);
		ResourceGenerator type = (ResourceGenerator) generator.getTypeClass();
		
		for(ResourceDeposit deposit : deposits){
			if(deposit.getTotalQuantity() < deposit.getYield()) {
				continue;
			}
			if(type.canGenerate(deposit.getTypeClass())) {
				int qty = (int) (deposit.getYield() * efficiency);
				if(qty > deposit.getTotalQuantity()) {
					qty = deposit.getTotalQuantity();
				}
				ColonyItem item = entityStore.getItem(colony, owner, deposit.getTypeClass());
				if(item == null) {
					item = new ColonyItem();
					item.setColony(colony);
					item.setItem(new Items(deposit.getTypeClass()));
					item.setOwner(owner);
					entityStore.create(item);
				}
				item.add(qty);
				deposit.remove(qty);
				entityStore.update(item);
				entityStore.update(deposit);
				if(log.isDebugEnabled())
					log.debug(generator + " generated " + qty + " x " + deposit.getType());
			}
			
		}
	}

}
