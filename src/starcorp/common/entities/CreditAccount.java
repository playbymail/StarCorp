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

import starcorp.common.types.CashTransaction;
import starcorp.common.types.GalacticDate;

/**
 * starcorp.common.entities.CreditAccount
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 22 Sep 2007
 */
public class CreditAccount {

	private long ID;
	private int version;
	private ABaseEntity accountHolder;
	private int credits;
	private Set<CashTransaction> transactions = new HashSet<CashTransaction>();
	public int add(int credits, GalacticDate date, String description) {
		this.credits += credits;
		transactions.add(new CashTransaction(date, credits,description));
		return this.credits;
	}
	
	public void remove(int credits, GalacticDate date, String description) {
		this.credits -= credits;
		transactions.add(new CashTransaction(date, (0 - credits),description));
	}
	
	public boolean hasCredits(int credits) {
		return this.credits >= credits;
	}
	
	public int getCredits() {
		return credits;
	}
	public void setCredits(int credits) {
		this.credits = credits;
	}
	public Set<CashTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<CashTransaction> transactions) {
		this.transactions = transactions;
	}

	public long getID() {
		return ID;
	}

	public void setID(long id) {
		ID = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public ABaseEntity getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(ABaseEntity accountHolder) {
		this.accountHolder = accountHolder;
	}

	public String toString()
	{
	    final String TAB = "    ";
	    
	    String retValue = "";
	    
	    retValue = "CreditAccount ( "
	        + super.toString() + TAB
	        + "ID = " + this.ID + TAB
	        + "version = " + this.version + TAB
	        + "accountHolder = " + this.accountHolder + TAB
	        + "credits = " + this.credits + TAB
	        + " )";
	
	    return retValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ID ^ (ID >>> 32));
		result = prime * result + version;
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
		final CreditAccount other = (CreditAccount) obj;
		if (ID != other.ID)
			return false;
		if (version != other.version)
			return false;
		return true;
	}
	
}
