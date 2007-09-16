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

import java.util.List;

import starcorp.common.types.GalacticDate;
import starcorp.common.types.AItemType;

/**
 * starcorp.common.entities.Corporation
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public class Corporation extends ANamedEntity {

	private String playerName;
	private String playerEmail;
	private int credits;
	private GalacticDate foundedDate;
	
	public ActionReport buyItem(AItemType type, int quantity, ColonyItem colonyItem, List<MarketItem> marketItems, Facility colonyHub) {
		// TODO buy item
		return null;
	}
	
	public ActionReport sellItem(AItemType type, int quantity, ColonyItem colonyItem, MarketItem marketItem, Facility colonyHub) {
		// TODO sell item
		return null;
	}
	
	public ActionReport buildFacility(Colony colony, Facility facility, List<ColonyItem> buildingModules, FacilityLease lease, DevelopmentGrant grant) {
		// TODO build facility
		return null;
	}
	
	public ActionReport buildShip(Colony colony, Starship ship, List<ColonyItem> shipHulls) {
		// TODO build ship
		return null;
	}

	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public String getPlayerEmail() {
		return playerEmail;
	}
	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}
	public int getCredits() {
		return credits;
	}
	public void setCredits(int credits) {
		this.credits = credits;
	}
	public GalacticDate getFoundedDate() {
		return foundedDate;
	}
	public void setFoundedDate(GalacticDate foundedDate) {
		this.foundedDate = foundedDate;
	}
}
