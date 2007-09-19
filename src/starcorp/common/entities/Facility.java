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
package starcorp.common.entities;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import starcorp.common.types.AFacilityType;
import starcorp.common.types.CashTransaction;
import starcorp.common.types.ColonyHub;
import starcorp.common.types.Factory;
import starcorp.common.types.GalacticDate;
import starcorp.common.types.Items;
import starcorp.common.types.OrbitalDock;
import starcorp.common.types.ServiceFacility;

/**
 * starcorp.common.entities.Facility
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public class Facility extends ABaseEntity {

	public static class ServiceResult {
		public int quantityServiced;
		public int totalCost;
	}
	
	private Corporation owner;
	private Colony colony;
	private AFacilityType type;
	private boolean powered;
	private int serviceCharge;
	private int transactionCount;
	private boolean open;
	
	private Set<Items> itemQueue = new HashSet<Items>();
	private Set<Items> itemsCreated = new HashSet<Items>();
	private GalacticDate builtDate;
	
	public static ServiceResult service(Map<Facility, List<Workers>> facilities, int quantity, int cashAvailable) {
		ServiceResult result = new ServiceResult();
		
		Iterator<Facility> i = facilities.keySet().iterator();
		while(i.hasNext() && result.quantityServiced < quantity) {
			Facility facility = i.next();
			List<Workers> workers = facilities.get(facility);
			int avail = facility.getTransactionsRemaining(workers);
			int qty = quantity - result.quantityServiced;
			int afford = cashAvailable / facility.getServiceCharge();
			if(afford < qty) {
				qty = afford;
			}
			if(avail < qty) {
				qty = avail;
			}
			AFacilityType type = facility.getTypeClass();
			int price = qty * facility.getServiceCharge();
			Colony colony = facility.getColony();
			Object[] args = {type.getName(), String.valueOf(qty), colony.getName(), String.valueOf(colony.getID())};
			String desc = CashTransaction.getDescription(CashTransaction.SERVICE_CHARGE, args);
			facility.getOwner().add(price, desc);
			facility.incTransactionCount();
			
			result.quantityServiced += qty;
			result.totalCost += price;
			cashAvailable -= price;
		}
		return result;
	}
	
	public double getEfficiency(List<Workers> currentWorkers) {
		return isPowered() ?  type.getEfficiency(currentWorkers) : 0.0;
	}
	
	public void createdItem(Items item) {
		itemsCreated.add(item);
	}
	
	public boolean queueItem(Items item) {
		if(type instanceof Factory) {
			Factory factory = (Factory) type;
			if(factory.canBuild(item.getTypeClass())) {
				itemQueue.add(item);
				return true;
			}
		}
		return false;
	}
	
	public Iterator<Items> created() {
		return itemsCreated.iterator();
	}
	
	public Iterator<Items> queue() {
		return itemQueue.iterator();
	}
	
	public void clearQueue() {
		itemQueue.clear();
	}
	
	public void clearCreated() {
		itemsCreated.clear();
	}
	
	public int getTransactionsRemaining(List<Workers> workers) {
		if(type instanceof Factory) {
			Factory factory = (Factory) type;
			return factory.getCapacity(workers) - transactionCount;
		}
		else if(type instanceof ServiceFacility) {
			ServiceFacility facility = (ServiceFacility) type;
			return facility.getTotalColonistsServiceable(workers) - transactionCount;
		}
		else if(type instanceof ColonyHub) {
			ColonyHub hub = (ColonyHub) type;
			return hub.getMaxMarketTransactions(workers) - transactionCount;
		}
		else if(type instanceof OrbitalDock) {
			OrbitalDock dock = (OrbitalDock) type;
			return dock.getMaxMarketTransactions(workers) - transactionCount;
		}
		else {
			return -1;
		}
	}
	
	public Corporation getOwner() {
		return owner;
	}
	public void setOwner(Corporation owner) {
		this.owner = owner;
	}
	public Colony getColony() {
		return colony;
	}
	public void setColony(Colony colony) {
		this.colony = colony;
	}
	public String getType() {
		return type == null ? null : type.getKey();
	}
	public void setType(String key) {
		type = AFacilityType.getType(key);
	}
	public AFacilityType getTypeClass() {
		return type;
	}
	public void setTypeClass(AFacilityType type) {
		this.type = type;
	}
	public int getServiceCharge() {
		return serviceCharge;
	}
	public void setServiceCharge(int serviceCharge) {
		this.serviceCharge = serviceCharge;
	}
	public int getTransactionCount() {
		return transactionCount;
	}
	
	public void incTransactionCount() {
		this.transactionCount++;
	}
	
	public void incTransactionCount(int qty) {
		this.transactionCount += qty;
	}
	
	public void setTransactionCount(int transactionCount) {
		this.transactionCount = transactionCount;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public Set<Items> getItemQueue() {
		return itemQueue;
	}
	public void setItemQueue(Set<Items> itemQueue) {
		this.itemQueue = itemQueue;
	}
	public GalacticDate getBuiltDate() {
		return builtDate;
	}

	public void setBuiltDate(GalacticDate builtDate) {
		this.builtDate = builtDate;
	}

	public boolean isPowered() {
		return powered;
	}

	public void setPowered(boolean powered) {
		this.powered = powered;
	}

	public Set<Items> getItemsCreated() {
		return itemsCreated;
	}

	public void setItemsCreated(Set<Items> itemsCreated) {
		this.itemsCreated = itemsCreated;
	}
}
