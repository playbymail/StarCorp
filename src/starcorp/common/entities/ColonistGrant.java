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
	private int credit;
	private boolean available;
	private GalacticDate issuedDate;
	
	public Colony getColony() {
		return colony;
	}
	public void setColony(Colony colony) {
		this.colony = colony;
	}
	public PopulationClass getPopClass() {
		return popClass;
	}
	public void setPopClass(PopulationClass popClass) {
		this.popClass = popClass;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
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
