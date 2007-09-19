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
import starcorp.common.entities.Corporation;
import starcorp.common.entities.GasField;
import starcorp.common.entities.ResourceDeposit;
import starcorp.common.entities.Starship;
import starcorp.common.turns.OrderReport;
import starcorp.common.turns.TurnError;
import starcorp.common.turns.TurnOrder;

/**
 * starcorp.server.turns.MineGasField
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class MineGasField extends AOrderProcessor {
	public static final int TIME_UNITS = 50;
	
	@Override
	public TurnError process(TurnOrder order) {
		TurnError error = null;
		Corporation corp = order.getCorp();
		int starshipId = order.getAsInt(0);
		int gasFieldId = order.getAsInt(1);
		
		Starship ship = (Starship) entityStore.load(starshipId);
		GasField gasfield = (GasField) entityStore.load(gasFieldId);
		
		if(ship == null || !ship.getOwner().equals(corp)) {
			error = new TurnError(TurnError.INVALID_SHIP);
		}
		else if(!ship.enoughTimeUnits(TIME_UNITS)) {
			error = new TurnError(TurnError.INSUFFICIENT_TIME);
		}
		else if(gasfield == null || !gasfield.getSystem().equals(ship.getSystem()) || !gasfield.getLocation().equals(ship.getLocation()) || ship.getPlanet() != null) {
			error = new TurnError(TurnError.INVALID_LOCATION);
		}
		else if(!ship.getDesign().canMineGasField()) {
			error = new TurnError(TurnError.INVALID_SHIP);
		}
		else {
			List<ResourceDeposit> deposits = entityStore.listDeposits(gasfield);
			Iterator<ResourceDeposit> i = deposits.iterator();
			while(i.hasNext()) {
				ResourceDeposit y = i.next();
				int qty = ship.addCargo(y.getTypeClass(), y.getYield());
				y.setTotalQuantity(y.getTotalQuantity() - qty);
			}
			ship.incrementTimeUnitsUsed(TIME_UNITS);
			OrderReport report = new OrderReport(order);
			report.setScannedSystemEntities(entityStore.listSystemEntities(ship.getSystem(),ship.getLocation()));
			report.add(gasfield.getName());
			report.add(gasfield.getID());
			order.setReport(report);
		}
		
		return error;
	}
}
