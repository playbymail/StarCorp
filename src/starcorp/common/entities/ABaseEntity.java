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

import java.sql.Timestamp;

import org.dom4j.Element;

/**
 * starcorp.common.entities.BaseEntity
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public abstract class ABaseEntity {
	
	public static ABaseEntity fromXML(Element e) {
		String className = "starcorp.common.entities." + e.attributeValue("class");
		ABaseEntity entity;
		try {
			entity = (ABaseEntity) Class.forName(className).newInstance();
			entity.readXML(e);
			return entity;
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	private int ID;
	private Timestamp lastModified;
	
	public ABaseEntity() {
		
	}
	
	public void readXML(Element e) {
		this.ID = Integer.parseInt(e.attributeValue("ID","0"));
	}
	
	public Element toBasicXML(Element parent) {
		Element root = parent.addElement("entity");
		root.addAttribute("ID", String.valueOf(ID));
		root.addAttribute("class", getClass().getSimpleName());
		return root;
	}
	
	public Element toFullXML(Element parent) {
		return toBasicXML(parent);
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int id) {
		ID = id;
	}
	public Timestamp getLastModified() {
		return lastModified;
	}
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ABaseEntity other = (ABaseEntity) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
}
