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
package starcorp.server;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import starcorp.common.entities.Colony;
import starcorp.common.entities.Facility;
import starcorp.common.entities.MarketItem;
import starcorp.common.entities.Workers;
import starcorp.common.entities.Facility.ServiceResult;
import starcorp.common.entities.MarketItem.BuyResult;
import starcorp.common.types.AFacilityType;
import starcorp.common.types.AItemType;
import starcorp.common.types.CashTransaction;
import starcorp.common.types.GalacticDate;
import starcorp.common.types.Items;
import starcorp.server.entitystore.IEntityStore;

/**
 * starcorp.server.Util
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 22 Sep 2007
 */
public class Util {
	public static ServiceResult service(GalacticDate date, Map<Facility, List<Workers>> facilities, int quantity, int cashAvailable, IEntityStore entityStore) {
		ServiceResult result = new ServiceResult();
		
		Iterator<Facility> i = facilities.keySet().iterator();
		while(i.hasNext() && result.quantityServiced < quantity) {
			Facility facility = i.next();
			List<?> workers = facilities.get(facility);
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
			entityStore.addCredits(facility.getOwner(), price, desc);
			facility.incTransactionCount();
			
			result.quantityServiced += qty;
			result.totalCost += price;
			cashAvailable -= price;
		}
		return result;
	}
	
	public static BuyResult buy(GalacticDate date, List<MarketItem> items, int quantity, int cashAvailable, IEntityStore entityStore) {
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
			entityStore.addCredits(item.getSeller(), price, desc);
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
	

}
