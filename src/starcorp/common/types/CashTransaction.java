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
package starcorp.common.types;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * starcorp.common.types.CashTransaction
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 18 Sep 2007
 */
public class CashTransaction {

	public static final String GRANT_RECEIVED = "grant.received";
	public static final String GRANT_PAID = "grant.paid";
	public static final String ITEM_BOUGHT = "item.bought";
	public static final String ITEM_SOLD = "item.sold";
	public static final String MARKET_FEES = "fees.market";
	public static final String SALARY_PAID = "salary.paid";
	public static final String REDUNDANCY_PAY = "salary.redundancy";
	public static final String SERVICE_CHARGE = "service.charge";
	
	protected static final ResourceBundle bundle = ResourceBundle.getBundle("cash");
	
	public static String getDescription(String key, Object[] args) {
		try {
			if(args != null) {
				return MessageFormat.format(bundle.getString(key), args);
			}
			else {
				return bundle.getString(key);
			}
		}
		catch(MissingResourceException e) {
			return key;
		}
		catch(NullPointerException e) {
			return "!" + key + "!";
		}
	}
	
	private int amount;
	private String description;
	private GalacticDate date;
	
	public CashTransaction() {
		
	}
	
	public CashTransaction(int amount, String description) {
		this.amount = amount;
		this.description = description;
		this.date = GalacticDate.getCurrentDate();
	}
	
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public GalacticDate getDate() {
		return date;
	}
	public void setDate(GalacticDate date) {
		this.date = date;
	}
	
}