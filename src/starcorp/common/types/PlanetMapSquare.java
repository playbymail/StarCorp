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

/**
 * starcorp.common.types.PlanetMapSquare
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public class PlanetMapSquare {

	private Coordinates2D location;
	private TerrainType terrain;
	public String getTerrain() {
		return terrain == null ? null : terrain.getKey();
	}
	
	public void setTerrain(String key) {
		terrain = TerrainType.getType(key);
	}
	
	public int getX(){
		return location == null ? 0 : location.getX();
	}
	
	public void setX(int x) {
		if(location == null) {
			location = new Coordinates2D();
		}
		location.setX(x);
	}
	
	public int getY(){
		return location == null ? 0 : location.getY();
	}
	
	public void setY(int y) {
		if(location == null) {
			location = new Coordinates2D();
		}
		location.setY(y);
	}
	
	public Coordinates2D getLocation() {
		return location;
	}
	public void setLocation(Coordinates2D location) {
		this.location = location;
	}
	public TerrainType getTerrainType() {
		return terrain;
	}
	public void setTerrainType(TerrainType terrain) {
		this.terrain = terrain;
	}
	
}
