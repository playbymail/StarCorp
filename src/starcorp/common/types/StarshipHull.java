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
 * starcorp.common.types.StarshipHull
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class StarshipHull extends AFactoryItem {
	
	public int getConsumerCapacity() {
		return Integer.parseInt(getResource(this, "capacity.consumer"));
	}
	
	public int getIndustrialCapacity() {
		return Integer.parseInt(getResource(this, "capacity.industrial"));
	}

	public int getModulesCapacity() {
		return Integer.parseInt(getResource(this, "capacity.modules"));
	}

	public int getOrganicsCapacity() {
		return Integer.parseInt(getResource(this, "capacity.organics"));
	}

	public int getLiquidGasCapacity() {
		return Integer.parseInt(getResource(this, "capacity.liquidgas"));
	}

	public int getThrustPower() {
		return Integer.parseInt(getResource(this, "thrust"));
	}

	public int getWarpFactor() {
		return Integer.parseInt(getResource(this, "warp"));
	}

	public boolean isAsteroidMining() {
		return Boolean.parseBoolean(getResource(this, "mining.asteroid"));
	}

	public boolean isGasFieldMining() {
		return Boolean.parseBoolean(getResource(this, "mining.gasfield"));
	}
	
	public int getLongScanner() {
		return Integer.parseInt(getResource(this, "scan.long"));
	}
	
	public boolean isShortScanner() {
		return Boolean.parseBoolean(getResource(this, "scan.short"));
	}
	
	public boolean isCommand() {
		return Boolean.parseBoolean(getResource(this, "command"));
	}

	public boolean isCrew() {
		return Boolean.parseBoolean(getResource(this, "crew"));
	}
	
	public boolean isSystemProbe() {
		return Boolean.parseBoolean(getResource(this, "probe.system"));
	}
	
	public boolean isPlanetProbe() {
		return Boolean.parseBoolean(getResource(this, "probe.planet"));
	}

	public boolean isBioLab() {
		return Boolean.parseBoolean(getResource(this, "lab.bio"));
	}

	public boolean isPhysicsLab() {
		return Boolean.parseBoolean(getResource(this, "lab.physics"));
	}

	public boolean isGeoLab() {
		return Boolean.parseBoolean(getResource(this, "lab.geo"));
	}
}
