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

import java.util.HashSet;
import java.util.Set;

import starcorp.common.types.GalacticDate;

/**
 * starcorp.common.entities.ActionReport
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class ActionReport extends ABaseEntity {

	private Corporation to;
	private Set<Corporation> cc = new HashSet<Corporation>();
	private ABaseEntity subject;
	private String msgKey;
	private boolean success;
	private Set<String> msgArgs = new HashSet<String>();
	private GalacticDate reportDate;
	
	public Corporation getTo() {
		return to;
	}
	public void setTo(Corporation to) {
		this.to = to;
	}
	public ABaseEntity getSubject() {
		return subject;
	}
	public void setSubject(ABaseEntity subject) {
		this.subject = subject;
	}
	public String getMsgKey() {
		return msgKey;
	}
	public void setMsgKey(String msgKey) {
		this.msgKey = msgKey;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Set<String> getMsgArgs() {
		return msgArgs;
	}
	public void setMsgArgs(Set<String> msgArgs) {
		this.msgArgs = msgArgs;
	}
	public GalacticDate getReportDate() {
		return reportDate;
	}
	public void setReportDate(GalacticDate reportDate) {
		this.reportDate = reportDate;
	}
	public Set<Corporation> getCc() {
		return cc;
	}
	public void setCc(Set<Corporation> cc) {
		this.cc = cc;
	}
}
