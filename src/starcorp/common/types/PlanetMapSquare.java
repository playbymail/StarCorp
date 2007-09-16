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
package starcorp.common.types;

import java.util.HashSet;
import java.util.Set;

/**
 * starcorp.common.types.PlanetMapSquare
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public class PlanetMapSquare {

	private Coordinates2D location;
	private TerrainType terrain;
	private Set<ResourceYield> resources = new HashSet<ResourceYield>();
	public Coordinates2D getLocation() {
		return location;
	}
	public void setLocation(Coordinates2D location) {
		this.location = location;
	}
	public TerrainType getTerrain() {
		return terrain;
	}
	public void setTerrain(TerrainType terrain) {
		this.terrain = terrain;
	}
	public Set<ResourceYield> getResources() {
		return resources;
	}
	public void setResources(Set<ResourceYield> resources) {
		this.resources = resources;
	}
	
}
