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

import starcorp.common.types.GalacticDate;

/**
 * starcorp.common.entities.FactoryQueueItem
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 24 Sep 2007
 */
public class FactoryQueueItem extends ACorporateItem {

	private Facility factory;
	private GalacticDate queuedDate;
	private int position;
	
	public Facility getFactory() {
		return factory;
	}
	public void setFactory(Facility factory) {
		this.factory = factory;
	}
	public GalacticDate getQueuedDate() {
		return queuedDate;
	}
	public void setQueuedDate(GalacticDate queuedDate) {
		this.queuedDate = queuedDate;
	}
	@Override
	public void readXML(Element e) {
		super.readXML(e);
		factory = new Facility();
		factory.readXML(e.element("factory").element("entity"));
		queuedDate = new GalacticDate(e.element("queued").element("date"));
	}
	@Override
	public Element toBasicXML(Element parent) {
		Element e = super.toBasicXML(parent);
		factory.toBasicXML(e.addElement("factory"));
		queuedDate.toXML(e.addElement("queued"));
		return e;
	}
	@Override
	public String toString() {
		return "Factory ["+ factory.getID() + "] Queue [" + position +"] "+ super.toString();
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
}
