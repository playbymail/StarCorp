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

import starcorp.client.turns.OrderReport;
import starcorp.client.turns.TurnError;
import starcorp.client.turns.TurnOrder;
import starcorp.common.entities.AStarSystemEntity;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.Starship;
import starcorp.common.types.CoordinatesPolar;

/**
 * starcorp.server.turns.BuildFacility
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class Move extends AOrderProcessor {

	/* (non-Javadoc)
	 * @see starcorp.server.turns.AOrderProcessor#process(starcorp.client.turns.TurnOrder)
	 */
	@Override
	public TurnError process(TurnOrder order) {
		TurnError error = null;
		Corporation corp = order.getCorp();
		int starshipId = order.getAsInt(0);
		int quadrant = order.getAsInt(1);
		int orbit = order.getAsInt(1);
		CoordinatesPolar targetLocation = new CoordinatesPolar();
		targetLocation.setX(quadrant);
		targetLocation.setY(orbit);
		
		Starship ship = (Starship) entityStore.load(starshipId);
				
		if(ship == null || !ship.getOwner().equals(corp)) {
			error = new TurnError(TurnError.INVALID_SHIP);
		}
		else {
			if(ship.getPlanet() != null) {
				error = new TurnError(TurnError.INVALID_LOCATION);
			}
			else {
				CoordinatesPolar shipLocation = ship.getLocation();
				double timeUnits = ship.getTimeUnitsRemaining();
				double speed = ship.getDesign().getImpulseSpeed();
				List<AStarSystemEntity> entities = new ArrayList<AStarSystemEntity>();
				while(!shipLocation.equals(targetLocation) && timeUnits >= speed) {
					if(shipLocation.getX() < targetLocation.getX()) {
						shipLocation.setX(shipLocation.getX() + 1);
					}
					else if(shipLocation.getX() > targetLocation.getX()) {
						shipLocation.setX(shipLocation.getX() - 1);
					}
					if(shipLocation.getY() < targetLocation.getY()) {
						shipLocation.setY(shipLocation.getY() + 1);
					}
					else if(shipLocation.getY() > targetLocation.getY()) {
						shipLocation.setY(shipLocation.getY() - 1);
					}
					timeUnits -= speed;
					List<AStarSystemEntity> scan = entityStore.listSystemEntities(ship.getSystem(), shipLocation);
					entities.addAll(scan);
				}
				ship.setLocation(shipLocation);
				ship.setTimeUnitsRemaining((int)timeUnits);
				OrderReport report = new OrderReport(order);
				report.add(ship.getName());
				report.add(ship.getID());
				report.setScannedSystemEntities(entities);
				order.setReport(report);
			}
		}
		
		return error;
	}

}
