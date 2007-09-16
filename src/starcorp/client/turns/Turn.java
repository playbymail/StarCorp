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
package starcorp.client.turns;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import starcorp.common.entities.Corporation;
import starcorp.common.types.GalacticDate;

/**
 * starcorp.client.turns.Turn
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class Turn {

	private GalacticDate submittedDate;
	private GalacticDate processedDate;
	private Corporation corporation;
	private List<TurnOrder> orders = new ArrayList<TurnOrder>();
	private List<TurnError> errors = new ArrayList<TurnError>();
	
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
	
	public GalacticDate getSubmittedDate() {
		return submittedDate;
	}
	public void setSubmittedDate(GalacticDate submittedDate) {
		this.submittedDate = submittedDate;
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
