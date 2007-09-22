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
import java.util.Set;

import org.dom4j.Element;

import starcorp.common.types.AFacilityType;
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
	
	public double getEfficiency(List<?> currentWorkers) {
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
	
	public int getTransactionsRemaining(List<?> workers) {
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

	@Override
	public void readXML(Element e) {
		super.readXML(e);
		this.owner = new Corporation();
		this.owner.readXML(e.element("owner").element("entity"));
		this.colony = new Colony();
		this.colony.readXML(e.element("colony").element("entity"));
		this.type = AFacilityType.getType(e.attributeValue("type"));
		this.powered = Boolean.parseBoolean(e.attributeValue("powered","false"));
		this.serviceCharge =Integer.parseInt(e.attributeValue("charge","0"));
		this.open = Boolean.parseBoolean(e.attributeValue("open","false"));
		this.builtDate = new GalacticDate(e.element("built").element("date"));
		
		for(Iterator i = e.element("production-queue").elementIterator("item"); i.hasNext();) {
			itemQueue.add(new Items((Element)i.next()));
		}
		for(Iterator i = e.element("items-created").elementIterator("item"); i.hasNext();) {
			itemsCreated.add(new Items((Element)i.next()));
		}
	}

	@Override
	public Element toBasicXML(Element parent) {
		Element e = super.toBasicXML(parent);
		owner.toBasicXML(e.addElement("owner"));
		colony.toBasicXML(e.addElement("colony"));
		e.addAttribute("type", type.getKey());
		e.addAttribute("powered", powered ? "true" : "false");
		e.addAttribute("charge", String.valueOf(serviceCharge));
		e.addAttribute("open", open ? "true" : "false");
		builtDate.toXML(e.addElement("built"));
		return e;
	}

	@Override
	public Element toFullXML(Element parent) {
		Element e = super.toFullXML(parent);
		Element queue = e.addElement("production-queue");
		Iterator<Items> i = itemQueue.iterator();
		while(i.hasNext()) {
			i.next().toXML(queue.addElement("item"));
		}
		Element created = e.addElement("items-created");
		i = itemsCreated.iterator();
		while(i.hasNext()) {
			i.next().toXML(created.addElement("item"));
		}
		return e;
	}

	@Override
	public String toString() {
		return type.getKey() + " " + super.toString() + " @ " + colony.getName() + " (" + colony.getID() + ")";
	}
}
