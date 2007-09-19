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
package starcorp.client.turns;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import starcorp.common.entities.AStarSystemEntity;
import starcorp.common.entities.Colony;
import starcorp.common.entities.Facility;
import starcorp.common.entities.Planet;
import starcorp.common.entities.StarSystem;
import starcorp.common.entities.Starship;
import starcorp.common.types.OrderType;
import starcorp.common.types.PlanetMapSquare;

/**
 * starcorp.client.turns.TurnReport
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 17 Sep 2007
 */
public class OrderReport {

	private OrderType type;
	private List<String> msgArgs = new ArrayList<String>();
	private List<StarSystem> scannedSystems;
	private List<Starship> scannedShips;
	private List<Colony> scannedColonies;
	private List<Facility> scannedFacilities;
	private List<AStarSystemEntity> scannedSystemEntities;
	private Planet scannedPlanet;
	private StarSystem scannedStar;
	private Colony scannedColony;
	private PlanetMapSquare scannedLocation;
	
	public OrderReport() {
		
	}
	
	public OrderReport(TurnOrder order) {
		this.type = order.getType();
	}
	
	public String getDescription() {
		return type.getDescription(msgArgs);
	}
	
	public void add(int msgArg) {
		msgArgs.add(String.valueOf(msgArg));
	}
	
	public void add(double msgArg) {
		msgArgs.add(String.valueOf(msgArg));
	}

	public void add(String msgArg) {
		msgArgs.add(msgArg);
	}
	
	public Iterator<String> iterateArgs() {
		return msgArgs.iterator();
	}
	
	public List<String> getMsgArgs() {
		return msgArgs;
	}
	public void setMsgArgs(List<String> msgArgs) {
		this.msgArgs = msgArgs;
	}

	public List<Starship> getScannedShips() {
		return scannedShips;
	}

	public void setScannedShips(List<Starship> scannedShips) {
		this.scannedShips = scannedShips;
	}

	public List<Colony> getScannedColonies() {
		return scannedColonies;
	}

	public void setScannedColonies(List<Colony> scannedColonies) {
		this.scannedColonies = scannedColonies;
	}

	public List<Facility> getScannedFacilities() {
		return scannedFacilities;
	}

	public void setScannedFacilities(List<Facility> scannedFacilities) {
		this.scannedFacilities = scannedFacilities;
	}

	public List<AStarSystemEntity> getScannedSystemEntities() {
		return scannedSystemEntities;
	}

	public void setScannedSystemEntities(
			List<AStarSystemEntity> scannedSystemEntities) {
		this.scannedSystemEntities = scannedSystemEntities;
	}

	public Planet getScannedPlanet() {
		return scannedPlanet;
	}

	public void setScannedPlanet(Planet scannedPlanet) {
		this.scannedPlanet = scannedPlanet;
	}

	public StarSystem getScannedStar() {
		return scannedStar;
	}

	public void setScannedStar(StarSystem scannedStar) {
		this.scannedStar = scannedStar;
	}

	public Colony getScannedColony() {
		return scannedColony;
	}

	public void setScannedColony(Colony scannedColony) {
		this.scannedColony = scannedColony;
	}

	public List<StarSystem> getScannedSystems() {
		return scannedSystems;
	}

	public void setScannedSystems(List<StarSystem> scannedSystems) {
		this.scannedSystems = scannedSystems;
	}

	public PlanetMapSquare getScannedLocation() {
		return scannedLocation;
	}

	public void setScannedLocation(PlanetMapSquare scannedLocation) {
		this.scannedLocation = scannedLocation;
	}

	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}
	
	
}
