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

import starcorp.common.entities.Corporation;
import starcorp.common.types.GalacticDate;

/**
 * starcorp.client.turns.Turn
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class Turn {

	private GalacticDate processedDate;
	private Corporation corporation;
	private List<TurnOrder> orders = new ArrayList<TurnOrder>();
	private List<TurnError> errors = new ArrayList<TurnError>();
	
	public Turn() {
		
	}
	
	public Turn(Corporation corp) {
		corporation = corp;
	}
	
	public Turn(Element root) {
		this.corporation = new Corporation();
		this.corporation.readXML(root.element("corporation").element("entity"));
		Element eDate = root.element("processed");
		if(eDate != null) {
			this.processedDate = new GalacticDate(eDate.element("date"));
		}
		for(Iterator<?> i = root.elementIterator("order"); i.hasNext();) {
			this.orders.add(new TurnOrder((Element)i.next()));
		}
		for(Iterator<?> i = root.elementIterator("error"); i.hasNext();) {
			this.errors.add(new TurnError((Element)i.next()));
		}
	}
	
	public Element toXML(Element parent) {
		Element root = parent.addElement("turn");
		corporation.toFullXML(root.addElement("corporation"));
		if(processedDate != null)
			processedDate.toXML(root.addElement("processed"));
		if(orders.size() > 0) {
			Element eOrders = root.addElement("orders");
			eOrders.addAttribute("size", String.valueOf(orders.size()));
			for(int i = 0; i < orders.size(); i++) {
				orders.get(i).toXML(eOrders);
			}
		}
		if(errors.size() > 0) {
			Element eErrors = root.addElement("errors");
			eErrors.addAttribute("size", String.valueOf(errors.size()));
			for(int i = 0; i < errors.size(); i++) {
				errors.get(i).toXML(eErrors);
			}
		}
		return root;
	}
	
	public void add(TurnOrder order) {
		orders.add(order);
	}
	
	public Iterator<TurnOrder> orders() {
		return orders.iterator();
	}
	
	public void add(TurnError error) {
		errors.add(error);
	}
	
	public Iterator<TurnError> errors() {
		return errors.iterator();
	}
	
	public GalacticDate getProcessedDate() {
		return processedDate;
	}
	public void setProcessedDate(GalacticDate processedDate) {
		this.processedDate = processedDate;
	}
	public Corporation getCorporation() {
		return corporation;
	}
	public void setCorporation(Corporation corporation) {
		this.corporation = corporation;
	}
	public List<TurnOrder> getOrders() {
		return orders;
	}
	public void setOrders(List<TurnOrder> orders) {
		this.orders = orders;
	}
	public List<TurnError> getErrors() {
		return errors;
	}
	public void setErrors(List<TurnError> errors) {
		this.errors = errors;
	}
}
