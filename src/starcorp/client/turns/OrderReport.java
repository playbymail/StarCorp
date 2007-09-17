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

/**
 * starcorp.client.turns.TurnReport
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 17 Sep 2007
 */
public class OrderReport {

	private String msgKey;
	private List<String> msgArgs = new ArrayList<String>();
	private List<ABaseEntity> reportItems = new ArrayList<ABaseEntity>();
	
	public OrderReport() {
		
	}
	
	public OrderReport(TurnOrder order) {
		this.msgKey = order.getType().getKey();
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
	
	public void add(ABaseEntity entity) {
		reportItems.add(entity);
	}
	
	public Iterator<String> iterateArgs() {
		return msgArgs.iterator();
	}
	
	public Iterator<ABaseEntity> iterateItems() {
		return reportItems.iterator();
	}
	
	public String getMsgKey() {
		return msgKey;
	}
	public void setMsgKey(String msgKey) {
		this.msgKey = msgKey;
	}
	public List<String> getMsgArgs() {
		return msgArgs;
	}
	public void setMsgArgs(List<String> msgArgs) {
		this.msgArgs = msgArgs;
	}
	public List<ABaseEntity> getReportItems() {
		return reportItems;
	}
	public void setReportItems(List<ABaseEntity> reportItems) {
		this.reportItems = reportItems;
	}
	
	
}
