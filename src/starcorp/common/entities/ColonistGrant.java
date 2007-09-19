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
import starcorp.common.types.PopulationClass;

/**
 * starcorp.common.entities.ColonistGrant
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class ColonistGrant extends ABaseEntity {

	private Colony colony;
	private PopulationClass popClass;
	private int credits;
	private boolean available;
	private GalacticDate issuedDate;
	
	public Colony getColony() {
		return colony;
	}
	public void setColony(Colony colony) {
		this.colony = colony;
	}
	public String getPopClassType() {
		return popClass == null ? null : popClass.getKey();
	}
	
	public void setPopClassType(String key) {
		popClass = PopulationClass.getType(key);
	}
	
	public PopulationClass getPopClass() {
		return popClass;
	}
	public void setPopClass(PopulationClass popClass) {
		this.popClass = popClass;
	}
	public int getCredits() {
		return credits;
	}
	public void setCredits(int credit) {
		this.credits = credit;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public GalacticDate getIssuedDate() {
		return issuedDate;
	}
	public void setIssuedDate(GalacticDate issuedDate) {
		this.issuedDate = issuedDate;
	}
}
