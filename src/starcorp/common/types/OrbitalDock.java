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

import java.util.Set;

/**
 * starcorp.common.types.OrbitalDock
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class OrbitalDock extends AFacilityType {
	public int getMaxMarketTransactions(Set<Colonists> currentWorkers) {
		double efficiency = getEfficiency(currentWorkers);
		
		if(efficiency < 1) {
			return 0;
		}
		else if(efficiency < 10) {
			return 5;
		}
		else if (efficiency < 25) {
			return 10;
		}
		else if (efficiency < 50) {
			return 25;
		}
		else if (efficiency < 75) {
			return 50;
		}
		else if (efficiency < 100) {
			return 100;
		}
		else {
			return -1; // unlimited
		}
	}

}