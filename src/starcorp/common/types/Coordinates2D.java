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
 * starcorp.common.types.Coordinates2D
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public class Coordinates2D {
	private int x;
	private int y;
	
	public Coordinates2D() {
		
	}
	
	public Coordinates2D(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getDistance(Coordinates2D other) {
		int distance = 0;
		Coordinates2D current = new Coordinates2D();
		current.x = this.x;
		current.y = this.y;
		while(!current.equals(other)) {
			if(current.x > other.x) {
				current.x -= 1;
			}
			else if(current.x < other.x) {
				current.x += 1;
			}
			if(current.y > other.y) {
				current.y -= 1;
			}
			else if(current.y < other.y) {
				current.y += 1;
			}
			distance++;
		}
		return distance;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		final Coordinates2D other = (Coordinates2D) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

}
