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
 * starcorp.server.turns.CorporationSellItem
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class CorporationSellItem extends AOrderProcessor {

	@Override
	public TurnError process(TurnOrder order) {
		TurnError error = null;
		OrderReport report = null;
		Corporation corp = order.getCorp();
		int colonyId = order.getAsInt(0);
		String itemTypeKey = order.get(1);
		int quantity = order.getAsInt(2);
		int price = order.getAsInt(3);
		
		Colony colony = (Colony) entityStore.load(colonyId);
		AItemType type = AItemType.getType(itemTypeKey);
		Facility colonyHub = entityStore.getFacility(colony, corp, ColonyHub.class);
		ColonyItem colonyItem = entityStore.getItem(colony, corp, type);
		
		if(colony == null) {
			error = new TurnError(TurnError.INVALID_COLONY);
		}
		else if(colonyHub == null) {
			error = new TurnError(TurnError.INVALID_COLONY);
		}
		else if(colonyItem == null) {
			error = new TurnError(TurnError.INVALID_ITEM);
		}
		else if(colonyHub.getTransactionsRemaining() < 1) {
			error = new TurnError(TurnError.MARKET_OUT_OF_TRANSACTIONS);
		}
		else {
			quantity = colonyItem.getItem().remove(quantity);
			Items item = new Items();
			item.setQuantity(quantity);
			item.setTypeClass(type);
			
			MarketItem marketItem = new MarketItem();
			marketItem.setColony(colony);
			marketItem.setCostPerItem(price);
			marketItem.setIssuedDate(GalacticDate.getCurrentDate());
			marketItem.setSeller(corp);
			marketItem.setItem(item);
			
			entityStore.save(marketItem);
			
			colonyHub.incTransactionCount();
			
			report = new OrderReport(order);
			report.add(quantity);
			report.add(type.getName());
			report.add(colony.getName());
			report.add(colony.getID());
			report.add(price);
			report.add(marketItem);
			report.add(colonyItem);
			order.setReport(report);
		}
		return error;
	}

}