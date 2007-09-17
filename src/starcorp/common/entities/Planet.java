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
import java.util.Iterator;
import java.util.Set;

import starcorp.common.types.AtmosphereType;
import starcorp.common.types.Coordinates2D;
import starcorp.common.types.PlanetMapSquare;

/**
 * starcorp.common.entities.Planet
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public class Planet extends AStarSystemEntity {

	private Planet orbiting;
	private AtmosphereType atmosphereType;
	private int gravityRating;
	private Set<PlanetMapSquare> map = new HashSet<PlanetMapSquare>();
	
	public PlanetMapSquare get(Coordinates2D location) {
		Iterator<PlanetMapSquare> i = map.iterator();
		while(i.hasNext()) {
			PlanetMapSquare sq = i.next();
			if(sq.getLocation().equals(location)) {
				return sq;
			}
		}
		return null;
	}
	
	public Planet set(PlanetMapSquare planetMapSquare) {
		Iterator<PlanetMapSquare> i = map.iterator();
		while(i.hasNext()) {
			PlanetMapSquare sq = i.next();
			if(sq.getLocation().equals(planetMapSquare.getLocation())) {
				i.remove();
			}
		}
		map.add(planetMapSquare);
		return this;
	}
	
	public Planet getOrbiting() {
		return orbiting;
	}
	public void setOrbiting(Planet orbiting) {
		this.orbiting = orbiting;
	}
	
	public String getAtmosphereType() {
		return atmosphereType.getKey();
	}
	
	public void setAtmosphereType(String key) {
		atmosphereType = AtmosphereType.getType(key);
	}
	
	public AtmosphereType getAtmosphereTypeClass() {
		return atmosphereType;
	}
	public void setAtmosphereTypeClass(AtmosphereType atmosphereType) {
		this.atmosphereType = atmosphereType;
	}
	public int getGravityRating() {
		return gravityRating;
	}
	public void setGravityRating(int gravityRating) {
		this.gravityRating = gravityRating;
	}
	public Set<PlanetMapSquare> getMap() {
		return map;
	}
	public void setMap(Set<PlanetMapSquare> map) {
		this.map = map;
	}
	
}
