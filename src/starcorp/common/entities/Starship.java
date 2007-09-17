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
import java.util.Set;

import starcorp.common.types.Coordinates2D;
import starcorp.common.types.GalacticDate;
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
