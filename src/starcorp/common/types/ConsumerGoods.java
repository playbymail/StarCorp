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
 * starcorp.common.types.ConsumerGoods
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class ConsumerGoods extends TradeGoods {
	public int getQuality() {
		return Integer.parseInt(getResource(this, "quality"));
	}
	
	public boolean isFood() {
		return Boolean.parseBoolean(getResource(this, "food"));
	}

	public boolean isDrink() {
		return Boolean.parseBoolean(getResource(this, "drink"));
	}
	
	public boolean isIntoxicant() {
		return Boolean.parseBoolean(getResource(this, "intoxicant"));
	}
	
	public boolean isClothes() {
		return Boolean.parseBoolean(getResource(this, "clothes"));
	}

}
