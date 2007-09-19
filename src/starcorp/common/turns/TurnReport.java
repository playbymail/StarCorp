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
package starcorp.common.turns;

import java.util.List;

import starcorp.common.entities.ColonistGrant;
import starcorp.common.entities.ColonyItem;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.DevelopmentGrant;
import starcorp.common.entities.Facility;
import starcorp.common.entities.FacilityLease;
import starcorp.common.entities.MarketItem;
import starcorp.common.entities.Starship;
import starcorp.common.entities.StarshipDesign;
import starcorp.common.types.GalacticDate;

/**
 * starcorp.client.turns.TurnReport
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class TurnReport {

	private GalacticDate reportDate;
	private Corporation to;
	private Turn turn;
	private List<ColonistGrant> colonistGrants;
	private List<ColonyItem> items;
	private List<DevelopmentGrant> developmentGrants;
	private List<Facility> facilities;
	private List<FacilityLease> leases;
	private List<MarketItem> markets;
	private List<Starship> starships;
	private List<StarshipDesign> designs;
	
	public TurnReport(Turn turn) {
		this.turn = turn;
		this.reportDate = GalacticDate.getCurrentDate();
	}
	
	public GalacticDate getTurnDate() {
		return reportDate;
	}
	public void setTurnDate(GalacticDate turnDate) {
		this.reportDate = turnDate;
	}
	public Corporation getTo() {
		return to;
	}
	public void setTo(Corporation to) {
		this.to = to;
	}
	public Turn getTurn() {
		return turn;
	}
	public void setTurn(Turn turn) {
		this.turn = turn;
	}
	public List<ColonistGrant> getColonistGrants() {
		return colonistGrants;
	}
	public void setColonistGrants(List<ColonistGrant> colonistGrants) {
		this.colonistGrants = colonistGrants;
	}
	public List<ColonyItem> getItems() {
		return items;
	}
	public void setItems(List<ColonyItem> items) {
		this.items = items;
	}
	public List<DevelopmentGrant> getDevelopmentGrants() {
		return developmentGrants;
	}
	public void setDevelopmentGrants(List<DevelopmentGrant> developmentGrants) {
		this.developmentGrants = developmentGrants;
	}
	public List<Facility> getFacilities() {
		return facilities;
	}
	public void setFacilities(List<Facility> facilities) {
		this.facilities = facilities;
	}
	public List<FacilityLease> getLeases() {
		return leases;
	}
	public void setLeases(List<FacilityLease> leases) {
		this.leases = leases;
	}
	public List<MarketItem> getMarkets() {
		return markets;
	}
	public void setMarkets(List<MarketItem> markets) {
		this.markets = markets;
	}
	public List<Starship> getStarships() {
		return starships;
	}
	public void setStarships(List<Starship> starships) {
		this.starships = starships;
	}
	public List<StarshipDesign> getDesigns() {
		return designs;
	}
	public void setDesigns(List<StarshipDesign> designs) {
		this.designs = designs;
	}
	
}
