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
import java.util.List;

import org.dom4j.Element;

import starcorp.common.types.GalacticDate;
import starcorp.common.types.Items;

/**
 * starcorp.common.entities.MarketItem
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class MarketItem extends ColonyItem {
	private int costPerItem;
	private GalacticDate issuedDate;
	private GalacticDate soldDate;
	
	public static class BuyResult {
		public int quantityBought;
		public int totalPrice;
		public List<Items> bought = new ArrayList<Items>();
	}
	
	public Corporation getSeller() {
		return getOwner();
	}
	public void setSeller(Corporation seller) {
		setOwner(seller);
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
		this.costPerItem = Integer.parseInt(e.attributeValue("price","0"));
		this.issuedDate = new GalacticDate(e.element("issued").element("date"));
		this.soldDate = new GalacticDate(e.element("sold").element("date"));
	}

	@Override
	public Element toBasicXML(Element parent) {
		Element e = super.toBasicXML(parent);
		e.addAttribute("price",String.valueOf(costPerItem));
		issuedDate.toXML(e.addElement("issued"));
		soldDate.toXML(e.addElement("sold"));
		return e;
	}
	@Override
	public String toString() {
		return "\u20a1 " + costPerItem + "ea. " + super.toString();
	}
	
}
