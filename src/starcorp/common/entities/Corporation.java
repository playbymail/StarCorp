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
 * starcorp.common.entities.Corporation
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public class Corporation extends ANamedEntity {

	private String playerName;
	private String playerEmail;
	private String playerPassword;
	private int credits;
	private GalacticDate foundedDate;
	private GalacticDate lastTurnDate;
	private Set<CashTransaction> transactions = new HashSet<CashTransaction>();
	
	public int add(int credits, String description) {
		this.credits += credits;
		transactions.add(new CashTransaction(credits,description));
		return this.credits;
	}
	
	public void remove(int credits, String description) {
		this.credits -= credits;
		transactions.add(new CashTransaction((0 - credits),description));
	}
	
	public boolean hasCredits(int credits) {
		return this.credits >= credits;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public String getPlayerEmail() {
		return playerEmail;
	}
	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}
	public int getCredits() {
		return credits;
	}
	public void setCredits(int credits) {
		this.credits = credits;
	}
	public GalacticDate getFoundedDate() {
		return foundedDate;
	}
	public void setFoundedDate(GalacticDate foundedDate) {
		this.foundedDate = foundedDate;
	}

	public String getPlayerPassword() {
		return playerPassword;
	}

	public void setPlayerPassword(String playerPassword) {
		this.playerPassword = playerPassword;
	}

	public GalacticDate getLastTurnDate() {
		return lastTurnDate;
	}

	public void setLastTurnDate(GalacticDate lastTurnDate) {
		this.lastTurnDate = lastTurnDate;
	}

	public Set<CashTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<CashTransaction> transactions) {
		this.transactions = transactions;
	}
}
