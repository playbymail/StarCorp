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
 * starcorp.common.types.CoordinatesPolar
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public class CoordinatesPolar {
	private int quadrant;
	private int orbit;
	
	public CoordinatesPolar() {
		
	}
	
	public CoordinatesPolar(int quadrant, int orbit) {
		this.quadrant = quadrant;
		this.orbit = orbit;
	}
	
	public int getDistance(CoordinatesPolar other) {
		int distance = 0;
		CoordinatesPolar current = new CoordinatesPolar();
		current.quadrant = this.quadrant;
		current.orbit = this.orbit;
		while(!current.equals(other)) {
			if(current.quadrant > other.quadrant) {
				current.quadrant -= 1;
			}
			else if(current.quadrant < other.quadrant) {
				current.quadrant += 1;
			}
			if(current.orbit > other.orbit) {
				current.orbit -= 1;
			}
			else if(current.orbit < other.orbit) {
				current.orbit += 1;
			}
			distance++;
		}
		return distance;
	}
	
	public int getQuadrant() {
		return quadrant;
	}
	public void setQuadrant(int x) {
		this.quadrant = x;
	}
	public int getOrbit() {
		return orbit;
	}
	public void setOrbit(int y) {
		this.orbit = y;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + quadrant;
		result = prime * result + orbit;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CoordinatesPolar other = (CoordinatesPolar) obj;
		if (quadrant != other.quadrant)
			return false;
		if (orbit != other.orbit)
			return false;
		return true;
	}

}
