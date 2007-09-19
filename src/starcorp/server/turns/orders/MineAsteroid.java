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
import java.util.Set;

import starcorp.client.turns.OrderReport;
import starcorp.client.turns.TurnError;
import starcorp.client.turns.TurnOrder;
import starcorp.common.entities.AsteroidField;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.Starship;
import starcorp.common.types.ResourceDeposit;

/**
 * starcorp.server.turns.MineAsteroid
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class MineAsteroid extends AOrderProcessor {
	public static final int TIME_UNITS = 50;
	
	@Override
	public TurnError process(TurnOrder order) {
		TurnError error = null;
		Corporation corp = order.getCorp();
		int starshipId = order.getAsInt(0);
		int asteroidId = order.getAsInt(1);
		
		Starship ship = (Starship) entityStore.load(starshipId);
		AsteroidField asteroid = (AsteroidField) entityStore.load(asteroidId);
		
		if(ship == null || !ship.getOwner().equals(corp)) {
			error = new TurnError(TurnError.INVALID_SHIP);
		}
		else if(!ship.enoughTimeUnits(TIME_UNITS)) {
			error = new TurnError(TurnError.INSUFFICIENT_TIME);
		}
		else if(asteroid == null || !asteroid.getSystem().equals(ship.getSystem()) || !asteroid.getLocation().equals(ship.getLocation()) || ship.getPlanet() != null) {
			error = new TurnError(TurnError.INVALID_LOCATION);
		}
		else if(!ship.getDesign().canMineAsteroid()) {
			error = new TurnError(TurnError.INVALID_SHIP);
		}
		else {
			Set<ResourceDeposit> deposits = asteroid.getResources();
			Iterator<ResourceDeposit> i = deposits.iterator();
			while(i.hasNext()) {
				ResourceDeposit y = i.next();
				int qty = ship.addCargo(y.getTypeClass(), y.getYield());
				y.setTotalQuantity(y.getTotalQuantity() - qty);
			}
			ship.incrementTimeUnitsUsed(TIME_UNITS);
			OrderReport report = new OrderReport(order);
			report.setScannedSystemEntities(entityStore.listSystemEntities(ship.getSystem(),ship.getLocation()));
			report.add(asteroid.getName());
			report.add(asteroid.getID());
			order.setReport(report);
		}
		
		return error;
	}
}
