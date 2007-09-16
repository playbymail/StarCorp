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

import java.util.ResourceBundle;

/**
 * starcorp.common.types.GalacticDate
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class GalacticDate {

	private static final ResourceBundle bundle = ResourceBundle.getBundle("date");

	private static GalacticDate currentDate;
	
	public static GalacticDate getCurrentDate() {
		if(currentDate == null) {
			int year = Integer.parseInt(bundle.getString("current.year"));
			int month = Integer.parseInt(bundle.getString("current.month"));
			currentDate = new GalacticDate(year,month);
		}
		return currentDate;
	}
	
	private int year;
	private int month;
	
	public GalacticDate() {
		
	}
	
	public GalacticDate(int year, int month) {
		this.year = year;
		this.month = month;
	}
	
	public GalacticDate(GalacticDate copyFrom) {
		this.year = copyFrom.year;
		this.month = copyFrom.month;
	}
	
	public boolean after(GalacticDate other) {
		if(this.year > other.year) {
			return true;
		}
		else if(this.year == other.year) {
			if(this.month > other.month) {
				return true;
			}
		}
		return false;
	}
	
	public boolean same(GalacticDate other) {
		return this.year == other.year && this.month == other.month;
	}
	
	public int monthsDiff(GalacticDate other) {
		int months = (other.year - this.year) * 12;
		months += (other.month - this.month);
		return months;
	}
	
	public int yearsDiff(GalacticDate other) {
		return other.year - this.year;
	}
	
	public GalacticDate add(int years, int months) {
		this.year += years;
		return add(months);
	}
	
	public GalacticDate add(int months) {
		this.month += months;
		if(this.month > 12) {
			int y = (this.month) / 12;
			this.year += y;
			this.month = this.month % 12;
		}
		return this;
	}
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	
	
}
