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

import java.util.List;

import starcorp.client.turns.OrderReport;
import starcorp.client.turns.TurnError;
import starcorp.client.turns.TurnOrder;
import starcorp.common.entities.Colony;
import starcorp.common.entities.ColonyItem;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.Facility;
import starcorp.common.entities.MarketItem;
import starcorp.common.entities.Workers;
import starcorp.common.types.AItemType;
import starcorp.common.types.CashTransaction;
import starcorp.common.types.ColonyHub;
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
		
		List<Workers> workers = entityStore.listWorkers(colonyHub);
		
		if(colony == null) {
			error = new TurnError(TurnError.INVALID_COLONY);
		}
		else if(colonyHub == null) {
			error = new TurnError(TurnError.INVALID_COLONY);
		}
		else if(colonyHub.getTransactionsRemaining(workers) < 1) {
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
			MarketItem.BuyResult result = MarketItem.buy(marketItems, quantity, corp.getCredits());
			Object[] args = {type.getName(), String.valueOf(result.quantityBought)};
			String desc = CashTransaction.getDescription(CashTransaction.ITEM_BOUGHT, args);
			colonyItem.getItem().add(result.quantityBought);
			corp.remove(result.totalPrice, desc);
			
			entityStore.save(colonyItem);
			desc = CashTransaction.getDescription(CashTransaction.MARKET_FEES, null);
			corp.remove(colonyHub.getServiceCharge(),desc);
			colonyHub.incTransactionCount();
			
			report = new OrderReport(order);
			report.add(result.quantityBought);
			report.add(type.getName());
			report.add(colony.getName());
			report.add(colony.getID());
			report.add(result.totalPrice);
			order.setReport(report);
		}
		return error;
	}

}
