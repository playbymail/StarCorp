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
	
	public static BuyResult buy(List<MarketItem> items, int quantity, int cashAvailable) {
		BuyResult result = new BuyResult();
		
		Iterator<MarketItem> i = items.iterator();
		while(i.hasNext() && result.quantityBought < quantity) {
			MarketItem item = i.next();
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
			item.getSeller().add(price, desc);
			item.getItem().remove(qty);
			if(item.getItem().getQuantity() < 1) {
				item.setSoldDate(GalacticDate.getCurrentDate());
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
}
