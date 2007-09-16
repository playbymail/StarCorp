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

import starcorp.common.types.AFacilityType;
import starcorp.common.types.GalacticDate;

/**
 * starcorp.common.entities.FacilityLease
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class FacilityLease extends ABaseEntity {

	private Colony colony;
	private AFacilityType type;
	private Corporation licensee;
	private int price;
	private boolean used;
	private GalacticDate issuedDate;
	private GalacticDate usedDate;
	
	public Colony getColony() {
		return colony;
	}
	public void setColony(Colony colony) {
		this.colony = colony;
	}
	public AFacilityType getType() {
		return type;
	}
	public void setType(AFacilityType type) {
		this.type = type;
	}
	public Corporation getLicensee() {
		return licensee;
	}
	public void setLicensee(Corporation licensee) {
		this.licensee = licensee;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public boolean isUsed() {
		return used;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
	public GalacticDate getIssuedDate() {
		return issuedDate;
	}
	public void setIssuedDate(GalacticDate issuedDate) {
		this.issuedDate = issuedDate;
	}
	public GalacticDate getUsedDate() {
		return usedDate;
	}
	public void setUsedDate(GalacticDate usedDate) {
		this.usedDate = usedDate;
	}
}
