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
package starcorp.common.entities;

import starcorp.common.types.GalacticDate;
import starcorp.common.types.Items;

/**
 * starcorp.common.entities.MarketItem
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class MarketItem extends ABaseEntity {

	private Colony colony;
	private Corporation seller;
	private Items item;
	private int costPerItem;
	private GalacticDate issuedDate;
	private GalacticDate soldDate;
	public Colony getColony() {
		return colony;
	}
	public void setColony(Colony colony) {
		this.colony = colony;
	}
	public Corporation getSeller() {
		return seller;
	}
	public void setSeller(Corporation seller) {
		this.seller = seller;
	}
	public Items getItem() {
		return item;
	}
	public void setItem(Items item) {
		this.item = item;
	}
	public int getCostPerItem() {
		return costPerItem;
	}
	public void setCostPerItem(int costPerItem) {
		this.costPerItem = costPerItem;
	}
	public GalacticDate getIssuedDate() {
		return issuedDate;
	}
	public void setIssuedDate(GalacticDate issuedDate) {
		this.issuedDate = issuedDate;
	}
	public GalacticDate getSoldDate() {
		return soldDate;
	}
	public void setSoldDate(GalacticDate soldDate) {
		this.soldDate = soldDate;
	}
}
