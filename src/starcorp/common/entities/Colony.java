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

import java.util.HashSet;
import java.util.Set;

import starcorp.common.types.ColonistHappiness;
import starcorp.common.types.Colonists;
import starcorp.common.types.Coordinates2D;
import starcorp.common.types.GalacticDate;

/**
 * starcorp.common.entities.Colony
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public class Colony extends ANamedEntity {

	private Corporation government;
	private Planet planet;
	private Coordinates2D location;
	private double hazardLevel;
	private Set<Colonists> unemployed = new HashSet<Colonists>();
	private Set<ColonistHappiness> happiness = new HashSet<ColonistHappiness>();
	private GalacticDate foundedDate;
	
	public ActionReport issueLease(FacilityLease lease) {
		// TODO issue lease
		return null;
	}
	
	public ActionReport issueColonistGrant(ColonistGrant grant) {
		// TODO issue colonist grant
		return null;
	}
	
	public ActionReport issueDevelopmentGrant(DevelopmentGrant grant) {
		// TODO issue development grant
		return null;
	}
	
	public Corporation getGovernment() {
		return government;
	}
	public void setGovernment(Corporation government) {
		this.government = government;
	}
	public Planet getPlanet() {
		return planet;
	}
	public void setPlanet(Planet planet) {
		this.planet = planet;
	}
	public Coordinates2D getLocation() {
		return location;
	}
	public void setLocation(Coordinates2D location) {
		this.location = location;
	}
	public double getHazardLevel() {
		return hazardLevel;
	}
	public void setHazardLevel(double hazardLevel) {
		this.hazardLevel = hazardLevel;
	}
	public Set<Colonists> getUnemployed() {
		return unemployed;
	}
	public void setUnemployed(Set<Colonists> unemployed) {
		this.unemployed = unemployed;
	}
	public Set<ColonistHappiness> getHappiness() {
		return happiness;
	}
	public void setHappiness(Set<ColonistHappiness> happiness) {
		this.happiness = happiness;
	}
	public GalacticDate getFoundedDate() {
		return foundedDate;
	}
	public void setFoundedDate(GalacticDate foundedDate) {
		this.foundedDate = foundedDate;
	}
	
}
