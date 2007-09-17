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
package starcorp.server.turns.orders;

import java.util.Iterator;
import java.util.List;

import starcorp.client.turns.OrderReport;
import starcorp.client.turns.TurnError;
import starcorp.client.turns.TurnOrder;
import starcorp.common.entities.Colony;
import starcorp.common.entities.ColonyItem;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.Facility;
import starcorp.common.entities.MarketItem;
import starcorp.common.types.AItemType;
import starcorp.common.types.ColonyHub;
import starcorp.common.types.GalacticDate;
import starcorp.common.types.Items;

/**
 * starcorp.server.turns.CorporationBuyItem
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class CorporationBuyItem extends AOrderProcessor {

	/* (non-Javadoc)
	 * @see starcorp.server.turns.AOrderProcessor#process(starcorp.client.turns.TurnOrder)
	 */
	@Override
	public TurnError process(TurnOrder order) {
		TurnError error = null;
		OrderReport report = null;
		Corporation corp = order.getCorp();
		int colonyId = order.getAsInt(0);
		String itemTypeKey = order.get(1);
		int quantity = order.getAsInt(2);
		
		Colony colony = (Colony) entityStore.load(colonyId);
		AItemType type = AItemType.getType(itemTypeKey);
		List<MarketItem> marketItems = entityStore.listMarket(colony, 1);
		Facility colonyHub = entityStore.getFacility(colony, colony.getGovernment(), ColonyHub.class);
		
		if(colony == null) {
			error = new TurnError(TurnError.INVALID_COLONY);
		}
		else if(colonyHub == null) {
			error = new TurnError(TurnError.INVALID_COLONY);
		}
		else if(colonyHub.getTransactionsRemaining() < 1) {
			error = new TurnError(TurnError.MARKET_OUT_OF_TRANSACTIONS);
		}
		else {	
			ColonyItem colonyItem = entityStore.getItem(colony, corp, type);
			if(colonyItem == null) {
				colonyItem = new ColonyItem();
				Items item = new Items();
				item.setTypeClass(type);
				colonyItem.setColony(colony);
				colonyItem.setItem(item);
				colonyItem.setOwner(corp);
			}
			int quantityBought = 0;
			int totalPrice = 0;
			Iterator<MarketItem> i = marketItems.iterator();
			while(i.hasNext() && quantityBought < quantity) {
				MarketItem item = i.next();
				int qty = quantity - quantityBought;
				int qtyAvail = item.getItem().getQuantity();
				int qtyAfford = corp.getCredits() / item.getCostPerItem();
				if(qtyAfford < qty) {
					qty = qtyAfford;
				}
				if(qtyAvail < qty) {
					qty = qtyAvail;
				}
				
				int price = qty * item.getCostPerItem();
				
				item.getSeller().add(price);
				
				item.getItem().remove(qty);
				if(item.getItem().getQuantity() < 1) {
					item.setSoldDate(GalacticDate.getCurrentDate());
				}
				
				quantityBought += qty;
				totalPrice += price;
			}
			colonyItem.getItem().add(quantityBought);
			corp.remove(totalPrice);
			
			entityStore.save(colonyItem);
			
			colonyHub.incTransactionCount();
			
			report = new OrderReport(order);
			report.add(quantityBought);
			report.add(type.getName());
			report.add(colony.getName());
			report.add(colony.getID());
			report.add(totalPrice);
			order.setReport(report);
		}
		return error;
	}

}
