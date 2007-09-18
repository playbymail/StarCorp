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
import starcorp.common.entities.Corporation;
import starcorp.common.entities.Planet;
import starcorp.common.entities.Starship;

/**
 * starcorp.server.turns.orders.DockAtColony
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 17 Sep 2007
 */
public class DockAtColony extends AOrderProcessor {
	public static final int TIME_UNITS = 10;
	
	@Override
	public TurnError process(TurnOrder order) {
		TurnError error = null;
		Corporation corp = order.getCorp();
		int starshipId = order.getAsInt(0);
		int colonyId = order.getAsInt(1);
		
		Starship ship = (Starship) entityStore.load(starshipId);
		Colony colony = (Colony) entityStore.load(colonyId);
		
		if(ship == null || !ship.getOwner().equals(corp)) {
			error = new TurnError(TurnError.INVALID_SHIP);
		}
		else if(colony == null) {
			error = new TurnError(TurnError.INVALID_LOCATION);
		}
		else if(!ship.enoughTimeUnits(TIME_UNITS)) {
			error = new TurnError(TurnError.INSUFFICIENT_TIME);
		}
		else {
			Planet planet = ship.getPlanet();
			if(planet == null) {
				error = new TurnError(TurnError.INVALID_LOCATION);
			}
			else if(!colony.getPlanet().equals(planet)) {
				error = new TurnError(TurnError.INVALID_LOCATION);
			}
			else if(planet.getGravityRating() > ship.getDesign().getMaxDockGravity()){
				error = new TurnError(TurnError.GRAVITY_TOO_HIGH);
			}
			else {
				ship.setPlanetLocation(colony.getLocation());
				ship.setColony(colony);
				ship.incrementTimeUnitsUsed(TIME_UNITS);
				OrderReport report = new OrderReport(order);
				report.add(ship.getName());
				report.add(ship.getID());
				report.add(colony.getName());
				report.add(colony.getID());
				report.setScannedColony(colony);
				report.setScannedFacilities(entityStore.listFacilities(colony));
				report.setScannedShips(entityStore.listShips(colony));
				order.setReport(report);
			}
		}
		
		return error;
	}

}