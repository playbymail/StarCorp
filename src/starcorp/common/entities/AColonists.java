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

import java.util.Iterator;
import java.util.List;

import starcorp.common.types.Population;
import starcorp.common.types.PopulationClass;

/**
 * starcorp.common.entities.AColonists
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 18 Sep 2007
 */
public abstract class AColonists extends ABaseEntity {

	public static int countColonists(List<AColonists> colonists) {
		int totalQty = 0;
		Iterator<AColonists> i = colonists.iterator();
		while(i.hasNext()) {
			AColonists colonist = i.next();
			totalQty += colonist.getQuantity();
		}
		return totalQty;
	}
	
	public static double getAverageHappiness(List<AColonists> colonists) {
		int totalQty = 0;
		double totalHappiness = 0.0;
		Iterator<AColonists> i = colonists.iterator();
		while(i.hasNext()) {
			AColonists colonist = i.next();
			totalQty += colonist.getQuantity();
			totalHappiness += (colonist.getQuantity() * colonist.getHappiness());
		}
		return totalHappiness / totalQty;
	}
	
	private Colony colony;
	private Population population;
	private int cash;
	private double happiness;

	public AColonists() {
		super();
	}
	
	public abstract Corporation getEmployer();
	
	public void addHappiness(double happiness) {
		this.happiness += happiness;
	}
	
	public void removeHappiness(double happiness) {
		this.happiness -= happiness;
	}
	
	public PopulationClass getPopClass() {
		return population.getPopClass();
	}
	
	public int getQuantity() {
		return population.getQuantity();
	}
	
	public int getCashPerPerson() {
		return cash / population.getQuantity();
	}
	
	public void addCash(int cash) {
		this.cash += cash;
	}
	
	public void removeCash(int cash) {
		this.cash -= cash;
	}

	public void addPopulation(int qty) {
		if(population != null) {
			population.add(qty);
		}
	}

	public int removePopulation(int qty) {
		if(population != null) {
			return population.remove(qty);
		}
		return 0;
	}

	public Colony getColony() {
		return colony;
	}

	public void setColony(Colony colony) {
		this.colony = colony;
	}

	public Population getPopulation() {
		return population;
	}

	public void setPopulation(Population colonists) {
		this.population = colonists;
	}

	public double getHappiness() {
		return happiness;
	}

	public void setHappiness(double happiness) {
		this.happiness = happiness;
	}

	public int getCash() {
		return cash;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}

}