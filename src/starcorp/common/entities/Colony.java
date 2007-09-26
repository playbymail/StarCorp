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

import org.dom4j.Element;

import starcorp.common.types.Coordinates2D;
import starcorp.common.types.GalacticDate;

/**
 * starcorp.common.entities.Colony
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public class Colony extends ANamedEntity {
	// TODO add planet name
	private Corporation government;
	private long planetID;
	private Coordinates2D location;
	private double hazardLevel;
	private GalacticDate foundedDate;
	
	public Corporation getGovernment() {
		return government;
	}
	public void setGovernment(Corporation government) {
		this.government = government;
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
	public GalacticDate getFoundedDate() {
		return foundedDate;
	}
	public void setFoundedDate(GalacticDate foundedDate) {
		this.foundedDate = foundedDate;
	}
	@Override
	public void readXML(Element e) {
		super.readXML(e);
		this.government = new Corporation();
		this.government.readXML(e.element("government").element("entity"));
//		this.planet = new Planet();
//		this.planet.readXML(e.element("planet").element("entity"));
		Element planet= e.element("planet");
		if(planet != null)
			this.planetID = Integer.parseInt(planet.attributeValue("ID","0"));
		this.location = new Coordinates2D(e);
		this.foundedDate = new GalacticDate(e.element("founded").element("date"));
		this.hazardLevel = Double.parseDouble(e.attributeValue("hazard","0.0"));
	}
	@Override
	public Element toBasicXML(Element parent) {
		Element e = super.toBasicXML(parent);
		government.toBasicXML(e.addElement("government"));
		Element planet = e.addElement("planet");
		planet.addAttribute("ID", String.valueOf(planetID));
		location.toXML(e);
		e.addAttribute("hazard", String.valueOf(hazardLevel));
		foundedDate.toXML(e.addElement("founded"));
		return e;
	}
	@Override
	public String toString() {
		return super.toString() + " @ " + planetID + ") " + location;
	}
	public long getPlanetID() {
		return planetID;
	}
	public void setPlanetID(long planetID) {
		this.planetID = planetID;
	}
	
}
