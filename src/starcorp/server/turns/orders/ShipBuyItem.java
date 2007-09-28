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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import starcorp.common.entities.AColonists;
import starcorp.common.entities.CashTransaction;
import starcorp.common.entities.Colony;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.Facility;
import starcorp.common.entities.MarketItem;
import starcorp.common.entities.Planet;
import starcorp.common.entities.Starship;
import starcorp.common.turns.OrderReport;
import starcorp.common.turns.TurnError;
import starcorp.common.turns.TurnOrder;
import starcorp.common.types.AItemType;
import starcorp.common.types.ColonyHub;
import starcorp.common.types.OrbitalDock;
import starcorp.common.types.OrderType;
import starcorp.server.ServerConfiguration;
import starcorp.server.Util;
import starcorp.server.turns.AOrderProcessor;

/**
 * starcorp.server.turns.ShipBuyItem
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class ShipBuyItem extends AOrderProcessor {
	private static final Log log = LogFactory.getLog(ShipBuyItem.class);
	
	public TurnError process(TurnOrder order) {
		TurnError error = null;
		OrderReport report = null;
		Corporation corp = order.getCorp();
		int starshipId = order.getAsInt(0);
		int colonyId = order.getAsInt(1);
		String itemTypeKey = order.get(2);
		int quantity = order.getAsInt(3);
		
		Starship ship = (Starship) entityStore.load(Starship.class, starshipId);
		Colony colony = (Colony) entityStore.load(Colony.class, colonyId);
		Planet colonyPlanet = null;
		if(colony != null)
			colonyPlanet = ((Planet) entityStore.load(Planet.class, colony.getPlanetID()));
		
		AItemType type = AItemType.getType(itemTypeKey);
		List<MarketItem> marketItems = entityStore.listMarket(colony, 1); 
		
		Facility colonyHub = entityStore.getFacility(colony, colony.getGovernment(), ColonyHub.class);
		Facility orbitalDock = entityStore.getFacility(colony, OrbitalDock.class);
		List<AColonists> hubWorkers = colonyHub == null ? null : entityStore.listWorkers(colonyHub);
		List<AColonists> dockWorkers = orbitalDock == null ? null : entityStore.listWorkers(orbitalDock);
		
		if(log.isDebugEnabled()) {
			log.debug("item: " + type + " mass each: " + type.getMassUnits());
			String s = "";
			if(hubWorkers != null) {
				s += " efficiency: " + colonyHub.getEfficiency(hubWorkers) + "  workers:";
				for(AColonists col: hubWorkers) {
					s += col + " ";
				}
			}
			log.debug("hub: " + colonyHub + s);
			s = "";
			if(dockWorkers != null) {
				s += " efficiency: " + orbitalDock.getEfficiency(dockWorkers) + "  workers:";
				for(AColonists col: dockWorkers) {
					s += col + " ";
				}
			}
			log.debug("dock: " + orbitalDock + s);
			
		}
		
		if(ship == null || !ship.getOwner().equals(corp)) {
			error = new TurnError(TurnError.INVALID_SHIP,order);
		}
		else if(colony == null) {
			error = new TurnError(TurnError.INVALID_COLONY,order);
		}
		else if(ship.getPlanet() == null || !ship.getPlanet().equals(colonyPlanet)) {
			error = new TurnError(TurnError.INVALID_LOCATION,order);
		}
		else if(ship.getColony() != null && !ship.getColony().equals(colony)) {
			error = new TurnError(TurnError.INVALID_COLONY,order);
		}
		else if(ship.getColony() == null && orbitalDock == null) {
			error = new TurnError(TurnError.INVALID_LOCATION,order);
		}
		else if(colonyHub == null) {
			error = new TurnError(TurnError.INVALID_COLONY,order);
		}
		else if(colonyHub.getTransactionsRemaining(hubWorkers) < 1) {
			error = new TurnError(TurnError.MARKET_OUT_OF_TRANSACTIONS,order);
		}
		else if(orbitalDock != null && ship.getColony() == null && orbitalDock.getTransactionsRemaining(dockWorkers) < 1) {
			error = new TurnError(TurnError.MARKET_OUT_OF_TRANSACTIONS,order);
		}
		else {
			int quantitySpaceFor = ship.getSpaceFor(type);
			if(log.isDebugEnabled()) {
				log.debug("Max Capacity for " + type + " " + ship.getCargoSpace(type) );
			}
			if(quantitySpaceFor < 1) {
				error = new TurnError(TurnError.INSUFFICIENT_SPACE,order);
			}
			else {
				if(log.isDebugEnabled()) {
					log.debug("Space: " + quantitySpaceFor + " Buying: " + quantity + " Ship: " + ship);
				}
				if(quantity > quantitySpaceFor) {
					quantity = quantitySpaceFor;
				}
				Util.BuyResult result = Util.buy(ServerConfiguration.getCurrentDate(), marketItems, quantity, entityStore.getCredits(corp),entityStore);
				if(log.isDebugEnabled()) {
					log.debug("Result: " + result);
				}
				Object[] args = {String.valueOf(result.quantityBought), type.getName(),colony.getName(),String.valueOf(colony.getID())};
				String desc = CashTransaction.getDescription(CashTransaction.ITEM_BOUGHT, args);
				ship.addCargo(type, result.quantityBought);
				entityStore.update(ship);
				entityStore.removeCredits(corp, result.totalPrice, desc);
				Object[] args2 = {colonyHub.getTypeClass().getName(), colony.getName(), String.valueOf(colony.getID())};
				desc = CashTransaction.getDescription(CashTransaction.MARKET_FEES, args2);
				entityStore.removeCredits(corp, colonyHub.getServiceCharge(), desc);
				colonyHub.incTransactionCount();
				entityStore.update(colonyHub);
				if(orbitalDock != null && ship.getColony() == null) {
					args2[0] = orbitalDock.getTypeClass().getName();
					desc = CashTransaction.getDescription(CashTransaction.MARKET_FEES, args2);
					entityStore.removeCredits(corp, orbitalDock.getServiceCharge(), desc);
					orbitalDock.incTransactionCount();
					entityStore.update(orbitalDock);
				}
				report = new OrderReport(order,colony,ship);
				report.add(result.quantityBought);
				report.add(type.getName());
				report.add(colony.getName());
				report.add(colony.getID());
				report.add(result.totalPrice);
				report.add(ship.getName());
				report.add(ship.getID());
				order.setReport(report);
			}
		}
		return error;
	}

	@Override
	public String getKey() {
		return OrderType.SHIP_BUY_ITEM;
	}

}
