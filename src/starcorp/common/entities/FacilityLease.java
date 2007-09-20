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
	
	@Override
	public void readXML(Element e) {
		super.readXML(e);
		this.colony = new Colony();
		colony.readXML(e.element("colony").element("entity"));
		this.licensee = new Corporation();
		Element eLicensee =e.element("licensee");
		if(eLicensee != null) {
			licensee.readXML(eLicensee.element("entity"));
		}
		this.type = AFacilityType.getType(e.attributeValue("type"));
		this.price = Integer.parseInt(e.attributeValue("price","0"));
		this.used = Boolean.parseBoolean(e.attributeValue("used","false"));
		this.issuedDate = new GalacticDate(e.element("issued").element("date"));
		this.usedDate = new GalacticDate(e.element("used").element("date"));
	}
	@Override
	public Element toBasicXML(Element parent) {
		Element e = super.toBasicXML(parent);
		colony.toBasicXML(e.addElement("colony"));
		if(licensee != null) {
			licensee.toBasicXML(e.addElement("licensee"));
		}
		e.addAttribute("type", type.getKey());
		e.addAttribute("price",String.valueOf(price));
		e.addAttribute("used",String.valueOf(used));
		issuedDate.toXML(e.addElement("issued"));
		usedDate.toXML(e.addElement("used"));
		return e;
	}
	@Override
	public String toString() {
		return type.getKey() + " " + super.toString() + " @ " + colony;
	}
	
}
