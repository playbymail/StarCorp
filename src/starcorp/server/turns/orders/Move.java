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

import starcorp.common.entities.Corporation;
import starcorp.common.entities.StarSystem;
import starcorp.common.entities.Starship;
import starcorp.common.turns.OrderReport;
import starcorp.common.turns.TurnError;
import starcorp.common.turns.TurnOrder;
import starcorp.common.types.CoordinatesPolar;
import starcorp.common.types.OrderType;
import starcorp.server.turns.AOrderProcessor;

/**
 * starcorp.server.turns.BuildFacility
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class Move extends AOrderProcessor {
	private static final Log log = LogFactory.getLog(Move.class);
	
	/* (non-Javadoc)
	 * @see starcorp.server.turns.AOrderProcessor#process(starcorp.client.turns.TurnOrder)
	 */
	public TurnError process(TurnOrder order) {
		TurnError error = null;
		Corporation corp = order.getCorp();
		int starshipId = order.getAsInt(0);
		int quadrant = order.getAsInt(1);
		int orbit = order.getAsInt(2);
		CoordinatesPolar targetLocation = new CoordinatesPolar();
		targetLocation.setQuadrant(quadrant);
		targetLocation.setOrbit(orbit);
		
		Starship ship = (Starship) entityStore.load(Starship.class, starshipId);
				
		if(ship == null || !ship.getOwner().equals(corp)) {
			error = new TurnError(TurnError.INVALID_SHIP,order);
			if(log.isDebugEnabled())
				log.debug(ship + " : " + corp);
		}
		else {
			if(ship.getPlanet() != null) {
				error = new TurnError(TurnError.INVALID_LOCATION,order);
			}
			else {
				StarSystem system = (StarSystem) entityStore.load(StarSystem.class, ship.getSystemID());
				CoordinatesPolar shipLocation = ship.getLocation();
				double timeUnits = ship.getTimeUnitsRemaining();
				double speed = ship.getDesign().getImpulseSpeed();
				if(log.isDebugEnabled()) {
					log.debug(ship +" : current: " + shipLocation + " : target: " + targetLocation + " : " + timeUnits +" TU : " + speed + " speed");
					log.debug("!shipLocation.equals(targetLocation) = " + !shipLocation.equals(targetLocation));
					log.debug("timeUnits >= speed = " + (timeUnits >= speed));
				}
				while(!shipLocation.equals(targetLocation) && timeUnits >= speed) {
					CoordinatesPolar previous = new CoordinatesPolar(shipLocation.getQuadrant(),shipLocation.getOrbit());
					if(shipLocation.getQuadrant() < targetLocation.getQuadrant()) {
						shipLocation.setQuadrant(shipLocation.getQuadrant() + 1);
					}
					else if(shipLocation.getQuadrant() > targetLocation.getQuadrant()) {
						shipLocation.setQuadrant(shipLocation.getQuadrant() - 1);
					}
					if(shipLocation.getOrbit() < targetLocation.getOrbit()) {
						shipLocation.setOrbit(shipLocation.getOrbit() + 1);
					}
					else if(shipLocation.getOrbit() > targetLocation.getOrbit()) {
						shipLocation.setOrbit(shipLocation.getOrbit() - 1);
					}
					timeUnits -= speed;
					if(log.isDebugEnabled()) {
						log.debug(ship + " moved to " + shipLocation + " from " + previous + " : " + timeUnits + " TU reminaing " + speed + " speed");
					}
				}
				ship.setLocation(shipLocation);
				ship.setTimeUnitsRemaining((int)timeUnits);
				entityStore.update(ship);
				OrderReport report = new OrderReport(order,null,ship);
				report.add(ship.getName());
				report.add(ship.getID());
				report.add(shipLocation.getQuadrant());
				report.add(shipLocation.getOrbit());
				report.add(system.getName());
				report.add(system.getID());
				List<?> scan = entityStore.listSystemEntities(system, shipLocation,ship);
				if(log.isDebugEnabled()) {
					for(Object o : scan) {
						log.debug("Scanned: " + o);
					}
				}
				report.addScannedEntities(scan);
				order.setReport(report);
			}
		}
		
		return error;
	}

	@Override
	public String getKey() {
		return OrderType.MOVE;
	}

}
