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
import java.util.List;
import java.util.Set;

import starcorp.common.types.ColonistSalary;
import starcorp.common.types.Colonists;
import starcorp.common.types.ColonyHub;
import starcorp.common.types.AFacilityType;
import starcorp.common.types.Factory;
import starcorp.common.types.GalacticDate;
import starcorp.common.types.Items;
import starcorp.common.types.OrbitalDock;
import starcorp.common.types.PopulationClass;
import starcorp.common.types.ServiceFacility;

/**
 * starcorp.common.entities.Facility
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public class Facility extends ABaseEntity {

	private Corporation owner;
	private Colony colony;
	private AFacilityType type;
	private Set<Colonists> workers = new HashSet<Colonists>();
	private Set<ColonistSalary> salaries = new HashSet<ColonistSalary>();
	private double efficiency;
	private int serviceCharge;
	private int transactionCount;
	private boolean open;
	private Set<Items> itemQueue = new HashSet<Items>();
	private GalacticDate builtDate;
	
	public ActionReport setSalary(PopulationClass popClass, int salary) {
		// TODO set salary
		return null;
	}
	
	public ActionReport close() {
		// TODO close
		return null;
	}
	
	public ActionReport payWorkers() {
		// TODO pay workers
		return null;
	}
	
	public ActionReport hireWorkers() {
		// TODO hire workers
		return null;
	}
	
	public ActionReport usePower(List<ColonyItem> corporationPowerItems, List<MarketItem> marketPowerItems) {
		// TODO use power
		return null;
	}
	
	public ActionReport queueItem(Items item) {
		// TODO queue item
		return null;
	}
	
	public Items nextQueuedItem() {
		return itemQueue.isEmpty() ? null : itemQueue.iterator().next();
	}
	
	public ActionReport produceNextItem(ColonyItem item) {
		// TODO produce item
		return null;
	}
	
	public ActionReport gatherItems(List<ColonyItem> gatheredItems) {
		// TODO gather items
		return null;
	}
	
	public int getTransactionsRemaining() {
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
	public AFacilityType getType() {
		return type;
	}
	public void setType(AFacilityType type) {
		this.type = type;
	}
	public Set<Colonists> getWorkers() {
		return workers;
	}
	public void setWorkers(Set<Colonists> workers) {
		this.workers = workers;
	}
	public double getEfficiency() {
		return efficiency;
	}
	public void setEfficiency(double efficiency) {
		this.efficiency = efficiency;
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
	public Set<ColonistSalary> getSalaries() {
		return salaries;
	}
	public void setSalaries(Set<ColonistSalary> salaries) {
		this.salaries = salaries;
	}

	public GalacticDate getBuiltDate() {
		return builtDate;
	}

	public void setBuiltDate(GalacticDate builtDate) {
		this.builtDate = builtDate;
	}
}
