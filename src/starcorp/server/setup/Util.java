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

/**
 * starcorp.server.setup.Util
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 21 Sep 2007
 */
public class Util {
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
