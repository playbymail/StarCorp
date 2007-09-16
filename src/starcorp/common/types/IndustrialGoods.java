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
 * starcorp.common.types.IndustrialGoods
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class IndustrialGoods extends TradeGoods {
	
	public boolean isLightAlloy() {
		return Boolean.parseBoolean(getResource(this, "alloy.light"));
	}

	public boolean isStrongAlloy() {
		return Boolean.parseBoolean(getResource(this, "alloy.strong"));
	}
	
	public boolean isConcrete() {
		return Boolean.parseBoolean(getResource(this, "concrete"));
	}
	
	public boolean isComputer() {
		return Boolean.parseBoolean(getResource(this, "computer"));
	}
	
	public boolean isNetwork() {
		return Boolean.parseBoolean(getResource(this, "network"));
	}

	public boolean isPlastic() {
		return Boolean.parseBoolean(getResource(this, "plastic"));
	}
	
	public boolean isPower() {
		return Boolean.parseBoolean(getResource(this, "power"));
	}
	
	public boolean isEngine() {
		return Boolean.parseBoolean(getResource(this, "engine"));
	}

}
