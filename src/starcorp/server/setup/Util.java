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
package starcorp.server.setup;

import java.util.Map;
import java.util.Random;

import starcorp.common.entities.Planet;
import starcorp.common.types.Coordinates2D;

/**
 * starcorp.server.setup.Util
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 21 Sep 2007
 */
public class Util {
	public static class SuitableLocation implements Comparable<SuitableLocation> {
		public Coordinates2D location;
		public Planet planet;
		public int rating;
		
		public int compareTo(SuitableLocation o) {
			return o.rating - this.rating;
		}
	
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((location == null) ? 0 : location.hashCode());
			result = prime * result
					+ ((planet == null) ? 0 : planet.hashCode());
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
			final SuitableLocation other = (SuitableLocation) obj;
			if (location == null) {
				if (other.location != null)
					return false;
			} else if (!location.equals(other.location))
				return false;
			if (planet == null) {
				if (other.planet != null)
					return false;
			} else if (!planet.equals(other.planet))
				return false;
			return true;
		} 
	}

	public static final Random rnd = new Random(System.currentTimeMillis());

	public static Object pick(Map<?, Integer> mapOfChoices) {
		Object[] choices = mapOfChoices.keySet().toArray();
		int[] chances = new int[choices.length];
		for(int i = 0; i < choices.length; i++) {
			chances[i] = mapOfChoices.get(choices[i]);
		}
		return pick(choices,chances);
	}

	public static Object pick(Object[] choices, int[] chances) {
		int rand = rnd.nextInt(100);
		int x = 0;
		int n = 0;
		for(int i : chances) {
			x += i;
			if(rand < x)
				return choices[n];
			n++;
		}
		return null;
	}
	

}
