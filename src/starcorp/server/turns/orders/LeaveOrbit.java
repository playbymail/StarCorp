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

import starcorp.common.entities.Corporation;
import starcorp.common.entities.Planet;
import starcorp.common.entities.Starship;
import starcorp.common.turns.OrderReport;
import starcorp.common.turns.TurnError;
import starcorp.common.turns.TurnOrder;

/**
 * starcorp.server.turns.LeaveOrbit
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class LeaveOrbit extends AOrderProcessor {
	public static final int TIME_UNITS = 5;
	
	@Override
	public TurnError process(TurnOrder order) {
		TurnError error = null;
		Corporation corp = order.getCorp();
		int starshipId = order.getAsInt(0);
		
		Starship ship = (Starship) entityStore.load(Starship.class, starshipId);
		
		if(ship == null || !ship.getOwner().equals(corp)) {
			error = new TurnError(TurnError.INVALID_SHIP);
		}
		else if(!ship.enoughTimeUnits(TIME_UNITS)) {
			error = new TurnError(TurnError.INSUFFICIENT_TIME);
		}
		else {
			if(ship.getPlanet() == null || ship.getPlanetLocation() != null) {
				error = new TurnError(TurnError.INVALID_LOCATION);
			}
			else {
				Planet planet = ship.getPlanet();
				ship.setPlanet(null);
				ship.incrementTimeUnitsUsed(TIME_UNITS);
				OrderReport report = new OrderReport(order);
				report.add(ship.getName());
				report.add(ship.getID());
				report.add(planet.getName());
				report.add(planet.getID());
				report.addScannedEntities(entityStore.listSystemEntities(ship.getSystem(), ship.getLocation()));
				order.setReport(report);
			}
		}
		
		return error;
		
	}

}
