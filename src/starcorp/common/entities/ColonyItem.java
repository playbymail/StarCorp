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

import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import starcorp.common.types.Items;

/**
 * starcorp.common.entities.ColonyItem
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class ColonyItem extends ABaseEntity {
	// TODO remove from ABaseEntity hierarchy
	public static int count(List<?> items) {
		int count = 0;
		for(Object o : items) {
			count += ((ColonyItem)o).getQuantity();
		}
		return count;
	}
	
	public static int use(List<?> items, int quantity) {
		int used = 0;
		Iterator<?> i = items.iterator();
		while(i.hasNext() && used < quantity) {
			ColonyItem item = (ColonyItem) i.next();
			int qty = quantity - used;
			if(qty > item.getQuantity()) {
				qty = item.getQuantity();
			}
			item.remove(qty);
		}
		return used;
	}
	
	private Corporation owner;
	private Colony colony;
	private Items item;
	
	public int getQuantity() {
		return item == null ? 0 : item.getQuantity();
	}
	
	public int add(int qty) {
		if(item == null) {
			return 0;
		}
		return item.add(qty);
	}
	
	public int remove(int qty) {
		if(item == null) {
			return 0;
		}
		return item.remove(qty);
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
	public Items getItem() {
		return item;
	}
	public void setItem(Items item) {
		this.item = item;
	}

	@Override
	public void readXML(Element e) {
		super.readXML(e);
		this.owner = new Corporation();
		this.owner.readXML(e.element("owner").element("entity"));
		this.colony = new Colony();
		this.colony.readXML(e.element("colony").element("entity"));
		this.item = new Items(e);
	}

	@Override
	public Element toBasicXML(Element parent) {
		Element e = super.toBasicXML(parent);
		owner.toBasicXML(e.addElement("owner"));
		colony.toBasicXML(e.addElement("colony"));
		item.toXML(e);
		return e;
	}

	@Override
	public String toString() {
		return item + " for " + owner.getName() +" (" + owner.getID() + ") " + super.toString() + " @ " + colony.getName() + " (" + colony.getID() + ")";
	}
}
