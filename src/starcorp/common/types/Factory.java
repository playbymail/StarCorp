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

import java.util.List;
import starcorp.common.entities.Workers;

/**
 * starcorp.common.types.Factory
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class Factory extends AFacilityType {

	public boolean isConsumer() {
		return Boolean.parseBoolean(getResource(this, "consumer"));
	}
	
	public boolean isIndustrial() {
		return Boolean.parseBoolean(getResource(this, "industrial"));
	}
	
	public boolean isConstruction() {
		return Boolean.parseBoolean(getResource(this, "construction"));
	}

	public boolean isShipard() {
		return Boolean.parseBoolean(getResource(this, "shipyard"));
	}

	public int getMaxCapacity() {
		return Integer.parseInt(getResource(this, "capacity"));
	}
	
	public int getCapacity(List<Workers> currentWorkers) {
		double efficiency = getEfficiency(currentWorkers);
		return (int) ((double) getMaxCapacity() * efficiency / 100);
	}
}
