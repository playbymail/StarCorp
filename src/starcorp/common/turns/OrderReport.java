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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import starcorp.common.entities.ABaseEntity;
import starcorp.common.types.OrderType;
import starcorp.common.types.PlanetMapSquare;

/**
 * starcorp.client.turns.TurnReport
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 17 Sep 2007
 */
public class OrderReport {

	private OrderType type;
	private List<String> msgArgs = new ArrayList<String>();
	@SuppressWarnings("unchecked")
	private List scannedEntities = new ArrayList();
	private PlanetMapSquare scannedLocation;
	
	public OrderReport() {
		
	}
	
	public OrderReport(TurnOrder order) {
		this.type = order.getType();
	}
	
	@SuppressWarnings("unchecked")
	public OrderReport(Element e) {
		this.type = OrderType.getType(e.attributeValue("type"));
		for(Iterator<?> i = e.elementIterator("report-arg"); i.hasNext();) {
			Element arg = (Element) i.next();
			msgArgs.set(Integer.parseInt(arg.attributeValue("position")), arg.getText());
		}
		Element scanned = e.element("scanned");
		for(Iterator<?> i = scanned.elementIterator("entity"); i.hasNext();) {
			ABaseEntity entity = ABaseEntity.fromXML((Element) i.next());
			if(entity != null)
				scannedEntities.add(entity);
		}
		Element eSquare = scanned.element("map-square");
		if(eSquare != null) {
			this.scannedLocation = new PlanetMapSquare(eSquare);
		}
	}
	
	public Element toXML(Element parent) {
		Element root = parent.addElement("order-report");
		root.addAttribute("type", type.getKey());
		for(int i = 0; i < msgArgs.size(); i++) {
			Element e = root.addElement("report-arg");
			e.addAttribute("position", String.valueOf(i));
			e.addText(msgArgs.get(i));
		}
		Element scanned = root.addElement("scanned");
		Iterator<?> i = scannedEntities.iterator();
		while(i.hasNext()) {
			ABaseEntity entity = (ABaseEntity) i.next();
			entity.toBasicXML(scanned);
		}
		if(scannedLocation != null) {
			scannedLocation.toXML(scanned);
		}
		return root;
	}
	
	public String getDescription() {
		return type.getDescription(msgArgs);
	}
	
	public void add(int msgArg) {
		msgArgs.add(String.valueOf(msgArg));
	}
	
	public void add(double msgArg) {
		msgArgs.add(String.valueOf(msgArg));
	}

	public void add(String msgArg) {
		msgArgs.add(msgArg);
	}
	
	public Iterator<String> iterateArgs() {
		return msgArgs.iterator();
	}
	
	public List<String> getMsgArgs() {
		return msgArgs;
	}
	public void setMsgArgs(List<String> msgArgs) {
		this.msgArgs = msgArgs;
	}

	public List<?> getScannedEntities() {
		return scannedEntities;
	}
	@SuppressWarnings("unchecked")
	public void addScannedEntities(List<?> scanned) {
		scannedEntities.addAll(scanned);
	}
	@SuppressWarnings("unchecked")
	public void addScannedEntity(ABaseEntity entity) {
		scannedEntities.add(entity);
	}

	public PlanetMapSquare getScannedLocation() {
		return scannedLocation;
	}

	public void setScannedLocation(PlanetMapSquare scannedLocation) {
		this.scannedLocation = scannedLocation;
	}

	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}
	
	
}
