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
package starcorp.common.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import starcorp.common.types.Coordinates2D;
import starcorp.common.types.GalacticDate;
import starcorp.common.types.AItemType;
import starcorp.common.types.Items;

/**
 * starcorp.common.entities.Starship
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public class Starship extends AStarSystemEntity {

	private Corporation owner;
	private Planet planet;
	private Coordinates2D planetLocation;
	private StarshipDesign design;
	private Set<Items> cargo = new HashSet<Items>();
	private GalacticDate builtDate;
	
	public ActionReport orbit(Planet planet) {
		// TODO orbit
		return null;
	}
	
	public ActionReport leaveOrbit() {
		// TODO leave orbit
		return null;
	}
	
	public ActionReport buyItem(AItemType type, int quantity, List<MarketItem> marketItems, Facility colonyHub, Facility orbitalDock) {
		// TODO buy item
		return null;
	}
	
	public ActionReport sellItem(AItemType type, int quantity, MarketItem marketItem, Facility colonyHub, Facility orbitalDock) {
		// TODO sell item
		return null;
	}
	
	public ActionReport move(Coordinates2D location) {
		// TODO move
		return null;
	}
	
	public ActionReport jump(StarSystem star) {
		// TODO jump
		return null;
	}
	
	public ActionReport mine(AsteroidField asteroid) {
		// TODO mine
		return null;
	}
	
	public ActionReport mine(GasField field) {
		// TODO mine
		return null;
	}
	
	public ActionReport scanGalaxy(List<StarSystem> systems) {
		// TODO scan galaxy
		return null;
	}
	
	public ActionReport scanSystem(List<AStarSystemEntity> scannableEntities) {
		// TODO scan system
		return null;
	}
	
	public ActionReport probe(AsteroidField asteroid) {
		// TODO probe
		return null;
	}
	
	public ActionReport probe(GasField field) {
		// TODO probe
		return null;
	}
	
	public ActionReport probe() {
		// TODO probe
		return null;
	}

	public ActionReport prospect() {
		// TODO prospect
		return null;
	}

	public ActionReport investigate(StellarAnomoly anomoly) {
		// TODO investigate
		return null;
	}
	
	public ActionReport foundColony(Facility colonyHub, Colony colony) {
		// TODO found colony
		return null;
	}

	public Corporation getOwner() {
		return owner;
	}
	public void setOwner(Corporation owner) {
		this.owner = owner;
	}
	public Planet getPlanet() {
		return planet;
	}
	public void setPlanet(Planet planet) {
		this.planet = planet;
	}
	public Coordinates2D getPlanetLocation() {
		return planetLocation;
	}
	public void setPlanetLocation(Coordinates2D planetLocation) {
		this.planetLocation = planetLocation;
	}
	public StarshipDesign getDesign() {
		return design;
	}
	public void setDesign(StarshipDesign design) {
		this.design = design;
	}
	public Set<Items> getCargo() {
		return cargo;
	}
	public void setCargo(Set<Items> cargo) {
		this.cargo = cargo;
	}
	public GalacticDate getBuiltDate() {
		return builtDate;
	}
	public void setBuiltDate(GalacticDate builtDate) {
		this.builtDate = builtDate;
	}
}
