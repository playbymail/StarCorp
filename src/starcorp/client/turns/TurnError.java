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
import java.util.List;

/**
 * starcorp.client.turns.TurnError
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class TurnError {

	public static final TurnError ERROR_AUTHORIZATION_FAILED = new TurnError("error.authorization");
	public static final TurnError ERROR_EARLY_TURN = new TurnError("error.earlyturn");
	
	public static final String INVALID_ORDER_TYPE = "error.order.type";
	public static final String INVALID_COLONY = "error.colony";
	public static final String INVALID_FACILITY_TYPE = "error.facility.type";
	public static final String NO_LEASE = "error.lease.none";
	public static final String INVALID_SHIP_DESIGN = "error.ship.design";
	
	private String msgKey;
	private TurnOrder order;
	private List<String> msgArgs = new ArrayList<String>();
	
	public TurnError() {
		
	}
	
	public TurnError(String msgKey) {
		this.msgKey = msgKey;
	}
	
	public TurnError(String msgKey, TurnOrder order) {
		this(msgKey);
		this.order = order;
	}
	
	public void addMsgArg(String arg) {
		msgArgs.add(arg);
	}
	
	public String getMsgKey() {
		return msgKey;
	}
	public void setMsgKey(String msgKey) {
		this.msgKey = msgKey;
	}
	public TurnOrder getOrder() {
		return order;
	}
	public void setOrder(TurnOrder order) {
		this.order = order;
	}
	public List<String> getMsgArgs() {
		return msgArgs;
	}
	public void setMsgArgs(List<String> msgArgs) {
		this.msgArgs = msgArgs;
	}
}
