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
 * starcorp.common.types.Resource
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public class Resources extends AItemType {
	
	public boolean isOrganic() {
		return getResourceAsBoolean(this, "organic");
	}

	public boolean isMinerals() {
		return getResourceAsBoolean(this, "mineral");
	}

	public boolean isMetal() {
		return getResourceAsBoolean(this, "metal");
	}

	public boolean isFissile() {
		return getResourceAsBoolean(this, "fissile");
	}

	public boolean isFuel() {
		return getResourceAsBoolean(this, "fuel");
	}

	public boolean isGas() {
		return getResourceAsBoolean(this, "gas");
	}

	public boolean isLiquid() {
		return getResourceAsBoolean(this, "liquid");
	}

	@Override
	public String getSubCategory() {
		if(isOrganic())
			return "Organic";
		if(isMinerals())
			return "Mineral";
		if(isMetal())
			return "Metal";
		if(isFissile())
			return "Fissile";
		if(isFuel())
			return "Fuel";
		if(isGas())
			return "Gas";
		if(isLiquid())
			return "Liquid";
		return "";
	}
}
