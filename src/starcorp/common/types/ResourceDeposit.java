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
 * starcorp.common.types.ResourceDeposit
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public class ResourceDeposit {

	private AItemType type;
	private int totalQuantity;
	private int yield;
	
	/**
	 * @return
	 */
	public String getType() {
		return type == null ? null : type.getKey();
	}
	
	/**
	 * @param key
	 */
	public void setType(String key) {
		type = AItemType.getType(key);
	}
	
	/**
	 * @return
	 */
	public AItemType getTypeClass() {
		return type;
	}
	
	/**
	 * @param type
	 */
	public void setTypeClass(AItemType type) {
		this.type = type;
	}
	
	/**
	 * @return
	 */
	public int getYield() {
		return yield;
	}
	/**
	 * @param quantity
	 */
	public void setYield(int quantity) {
		this.yield = quantity;
	}
	
	/**
	 * @param qty
	 * @return
	 */
	public int add(int qty) {
		yield += qty;
		return yield;
	}
	
	/**
	 * @param qty
	 * @return
	 */
	public int remove(int qty) {
		if(qty > yield) {
			qty = yield;
		}
		yield -= qty;
		return qty;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + yield;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		final ResourceDeposit other = (ResourceDeposit) obj;
		if (yield != other.yield)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
}
