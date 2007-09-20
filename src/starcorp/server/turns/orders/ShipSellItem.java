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

import starcorp.common.entities.Colony;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.Facility;
import starcorp.common.entities.MarketItem;
import starcorp.common.entities.Starship;
import starcorp.common.turns.OrderReport;
import starcorp.common.turns.TurnError;
import starcorp.common.turns.TurnOrder;
import starcorp.common.types.AItemType;
import starcorp.common.types.CashTransaction;
import starcorp.common.types.ColonyHub;
import starcorp.common.types.Items;
import starcorp.common.types.OrbitalDock;
import starcorp.common.types.OrderType;
import starcorp.server.ServerConfiguration;
import starcorp.server.turns.AOrderProcessor;

/**
 * starcorp.server.turns.BuildFacility
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class ShipSellItem extends AOrderProcessor {

	public TurnError process(TurnOrder order) {
		TurnError error = null;
		OrderReport report = null;
		Corporation corp = order.getCorp();
		int starshipId = order.getAsInt(0);
		int colonyId = order.getAsInt(1);
		String itemTypeKey = order.get(2);
		int quantity = order.getAsInt(3);
		int price = order.getAsInt(4);
		
		Starship ship = (Starship) entityStore.load(Starship.class, starshipId);
		Colony colony = (Colony) entityStore.load(Colony.class, colonyId);
		AItemType type = AItemType.getType(itemTypeKey);
		
		Facility colonyHub = entityStore.getFacility(colony, colony.getGovernment(), ColonyHub.class);
		Facility orbitalDock = entityStore.getFacility(colony, OrbitalDock.class);
		List<?> hubWorkers = colonyHub == null ? null : entityStore.listWorkers(colonyHub);
		List<?> dockWorkers = orbitalDock == null ? null : entityStore.listWorkers(orbitalDock);
		
		if(ship == null || !ship.getOwner().equals(corp)) {
			error = new TurnError(TurnError.INVALID_SHIP);
		}
		else if(colony == null) {
			error = new TurnError(TurnError.INVALID_COLONY);
		}
		else if(ship.getPlanet() == null || !ship.getPlanet().equals(colony.getPlanet())) {
			error = new TurnError(TurnError.INVALID_LOCATION);
		}
		else if(ship.getColony() != null && !ship.getColony().equals(colony)) {
			error = new TurnError(TurnError.INVALID_COLONY);
		}
		else if(ship.getColony() == null && orbitalDock == null) {
			error = new TurnError(TurnError.INVALID_LOCATION);
		}
		else if(colonyHub == null) {
			error = new TurnError(TurnError.INVALID_COLONY);
		}
		else if(colonyHub.getTransactionsRemaining(hubWorkers) < 1) {
			error = new TurnError(TurnError.MARKET_OUT_OF_TRANSACTIONS);
		}
		else if(orbitalDock != null && ship.getColony() == null && orbitalDock.getTransactionsRemaining(dockWorkers) < 1) {
			error = new TurnError(TurnError.MARKET_OUT_OF_TRANSACTIONS);
		}
		else {
			Items cargo = ship.getCargo(type);
			if(quantity > cargo.getQuantity()) {
				quantity = cargo.getQuantity();
			}
			MarketItem item = new MarketItem();
			item.setColony(colony);
			item.setCostPerItem(price);
			item.setIssuedDate(ServerConfiguration.getCurrentDate());
			item.setSeller(corp);
			entityStore.save(item);
			
			ship.removeCargo(type, quantity);
			Object[] args2 = {colonyHub.getTypeClass().getName(), colony.getName(), String.valueOf(colony.getID())};
			String desc = CashTransaction.getDescription(CashTransaction.MARKET_FEES, args2);
			corp.remove(colonyHub.getServiceCharge(),ServerConfiguration.getCurrentDate(),desc);
			colonyHub.incTransactionCount();
			if(orbitalDock != null && ship.getColony() == null) {
				args2[0] = orbitalDock.getTypeClass().getName();
				 desc = CashTransaction.getDescription(CashTransaction.MARKET_FEES, args2);
				corp.remove(orbitalDock.getServiceCharge(),ServerConfiguration.getCurrentDate(),desc);
				orbitalDock.incTransactionCount();
			}
			report = new OrderReport(order);
			report.add(quantity);
			report.add(price);
			report.add(type.getName());
			report.add(colony.getName());
			report.add(colony.getID());
			report.add(ship.getName());
			report.add(ship.getID());
			order.setReport(report);
		}
		return error;
	}

	@Override
	public String getKey() {
		return OrderType.SHIP_SELL_ITEM;
	}

}
