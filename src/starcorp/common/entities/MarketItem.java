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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import starcorp.common.types.AItemType;
import starcorp.common.types.CashTransaction;
import starcorp.common.types.GalacticDate;
import starcorp.common.types.Items;

/**
 * starcorp.common.entities.MarketItem
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class MarketItem extends ABaseEntity {

	private Colony colony;
	private Corporation seller;
	private Items item;
	private int costPerItem;
	private GalacticDate issuedDate;
	private GalacticDate soldDate;
	
	public static class BuyResult {
		public int quantityBought;
		public int totalPrice;
		public List<Items> bought = new ArrayList<Items>();
	}
	
	public static BuyResult buy(GalacticDate date, List<?> items, int quantity, int cashAvailable) {
		BuyResult result = new BuyResult();
		
		Iterator<?> i = items.iterator();
		while(i.hasNext() && result.quantityBought < quantity) {
			MarketItem item = (MarketItem) i.next();
			Colony colony = item.getColony();
			AItemType type = item.getItem().getTypeClass();
			int qty = quantity - result.quantityBought;
			int avail = item.getItem().getQuantity();
			int afford = cashAvailable / item.getCostPerItem();
			if(afford < qty) {
				qty = afford;
			}
			if(avail < qty) {
				qty = avail;
			}
			int price = qty * item.getCostPerItem();
			
			Object[] args = {String.valueOf(qty), type.getName(),colony.getName(),String.valueOf(colony.getID())};
			String desc = CashTransaction.getDescription(CashTransaction.ITEM_SOLD, args);
			item.getSeller().add(price, date, desc);
			item.getItem().remove(qty);
			if(item.getItem().getQuantity() < 1) {
				item.setSoldDate(date);
			}
			result.quantityBought += qty;
			result.totalPrice += price;
			cashAvailable -= price;
			result.bought.add(new Items(type,qty));
		}
		
		return result;
	}
	
	public Colony getColony() {
		return colony;
	}
	public void setColony(Colony colony) {
		this.colony = colony;
	}
	public Corporation getSeller() {
		return seller;
	}
	public void setSeller(Corporation seller) {
		this.seller = seller;
	}
	public Items getItem() {
		return item;
	}
	public void setItem(Items item) {
		this.item = item;
	}
	public int getCostPerItem() {
		return costPerItem;
	}
	public void setCostPerItem(int costPerItem) {
		this.costPerItem = costPerItem;
	}
	public GalacticDate getIssuedDate() {
		return issuedDate;
	}
	public void setIssuedDate(GalacticDate issuedDate) {
		this.issuedDate = issuedDate;
	}
	public GalacticDate getSoldDate() {
		return soldDate;
	}
	public void setSoldDate(GalacticDate soldDate) {
		this.soldDate = soldDate;
	}
	
	@Override
	public void readXML(Element e) {
		super.readXML(e);
		this.seller = new Corporation();
		this.seller.readXML(e.element("seller").element("entity"));
		this.colony = new Colony();
		this.colony.readXML(e.element("colony").element("entity"));
		this.item = new Items(e);
		this.costPerItem = Integer.parseInt(e.attributeValue("price","0"));
		this.issuedDate = new GalacticDate(e.element("issued").element("date"));
		this.soldDate = new GalacticDate(e.element("sold").element("date"));
	}

	@Override
	public Element toBasicXML(Element parent) {
		Element e = super.toBasicXML(parent);
		seller.toBasicXML(e.addElement("seller"));
		colony.toBasicXML(e.addElement("colony"));
		item.toXML(e);
		e.addAttribute("price",String.valueOf(costPerItem));
		issuedDate.toXML(e.addElement("issued"));
		soldDate.toXML(e.addElement("sold"));
		return e;
	}
	@Override
	public String toString() {
		return item + " \u20a1 " + costPerItem + "ea. " + super.toString() + " @ " + colony;
	}
	
}
