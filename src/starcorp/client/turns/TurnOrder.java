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

import starcorp.common.entities.ABaseEntity;
import starcorp.common.entities.ActionReport;
import starcorp.common.entities.Corporation;
import starcorp.common.types.OrderType;

/**
 * starcorp.client.turns.TurnOrder
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class TurnOrder {

	private Corporation corp;
	private ABaseEntity subject;
	private OrderType type;
	private List<String> args = new ArrayList<String>(); 
	private ActionReport report;
	
	public void add(String arg) {
		args.add(arg);
	}
	
	public Iterator<String> args() {
		return args.iterator();
	}
	
	public String get(int index) {
		return args.get(index);
	}
	
	public void set(int index, String value) {
		args.set(index, value);
	}
	
	public int getAsInt(int index) {
		try {
			return Integer.parseInt(get(index));
		}
		catch(NumberFormatException e) {
			return -1;
		}
		
	}
	
	public void set(int index, int value) {
		try {
			set(index, String.valueOf(value));
		}
		catch(NumberFormatException e) {
			set(index, "-1");
		}
	}
	
	public Corporation getCorp() {
		return corp;
	}
	public void setCorp(Corporation corp) {
		this.corp = corp;
	}
	public ABaseEntity getSubject() {
		return subject;
	}
	public void setSubject(ABaseEntity subject) {
		this.subject = subject;
	}
	public List<String> getArgs() {
		return args;
	}
	public void setArgs(List<String> args) {
		this.args = args;
	}
	public ActionReport getReport() {
		return report;
	}
	public void setReport(ActionReport report) {
		this.report = report;
	}
	public OrderType getType() {
		return type;
	}
	public void setType(OrderType type) {
		this.type = type;
	}
}
