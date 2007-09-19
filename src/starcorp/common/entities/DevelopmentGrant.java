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
 * starcorp.common.entities.DevelopmentGrant
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class DevelopmentGrant extends ABaseEntity {

	private Colony colony;
	private AFacilityType type;
	private int grant;
	private boolean used;
	private GalacticDate issuedDate;
	private GalacticDate usedDate;
	public Colony getColony() {
		return colony;
	}
	public void setColony(Colony colony) {
		this.colony = colony;
	}
	public String getType() {
		return type == null ? null : type.getKey();
	}
	public void setType(String key) {
		type = AFacilityType.getType(key);
	}
	public AFacilityType getTypeClass() {
		return type;
	}
	public void setTypeClass(AFacilityType type) {
		this.type = type;
	}
	public int getGrant() {
		return grant;
	}
	public void setGrant(int grant) {
		this.grant = grant;
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
