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
import starcorp.common.entities.Starship;
import starcorp.common.types.AItemType;
import starcorp.common.types.ColonyHub;
import starcorp.common.types.OrbitalDock;

/**
 * starcorp.server.turns.orders.PickupItem
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 17 Sep 2007
 */
public class PickupItem extends AOrderProcessor {

	public TurnError process(TurnOrder order) {
		TurnError error = null;
		OrderReport report = null;
		Corporation corp = order.getCorp();
		int starshipId = order.getAsInt(0);
		int colonyId = order.getAsInt(1);
		String itemTypeKey = order.get(2);
		int quantity = order.getAsInt(3);
		
		Starship ship = (Starship) entityStore.load(starshipId);
		Colony colony = (Colony) entityStore.load(colonyId);
		AItemType type = AItemType.getType(itemTypeKey);
		ColonyItem item = entityStore.getItem(colony, corp, type);
		Facility colonyHub = entityStore.getFacility(colony, colony.getGovernment(), ColonyHub.class);
		Facility orbitalDock = entityStore.getFacility(colony, OrbitalDock.class);
		
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
		else if(colonyHub.getTransactionsRemaining() < 1) {
			error = new TurnError(TurnError.MARKET_OUT_OF_TRANSACTIONS);
		}
		else if(orbitalDock != null && ship.getColony() == null && orbitalDock.getTransactionsRemaining() < 1) {
			error = new TurnError(TurnError.MARKET_OUT_OF_TRANSACTIONS);
		}
		else if(item == null || item.getItem().getQuantity() < 1) {
			error = new TurnError(TurnError.INVALID_ITEM);
		}
		else {
			int quantitySpaceFor = ship.getSpaceFor(type);
			if(quantity > quantitySpaceFor) {
				quantity = quantitySpaceFor;
			}
			int quantityAvailable = item.getItem().getQuantity();
			if(quantityAvailable < quantity) {
				quantity = quantityAvailable;
			}
			ship.addCargo(type, quantity);
			item.getItem().remove(quantity);
			
			corp.remove(colonyHub.getServiceCharge());
			colonyHub.incTransactionCount();
			if(orbitalDock != null && ship.getColony() == null) {
				corp.remove(orbitalDock.getServiceCharge());
				orbitalDock.incTransactionCount();
			}
			report = new OrderReport(order);
			report.add(ship.getName());
			report.add(ship.getID());
			report.add(quantity);
			report.add(type.getName());
			report.add(colony.getName());
			report.add(colony.getID());
			order.setReport(report);
		}
		return error;
	}
}
