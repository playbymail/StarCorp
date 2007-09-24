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
package starcorp.server.turns.orders;

import starcorp.common.entities.Colony;
import starcorp.common.entities.Facility;
import starcorp.common.entities.Unemployed;
import starcorp.common.entities.Workers;
import starcorp.common.turns.OrderReport;
import starcorp.common.turns.TurnError;
import starcorp.common.turns.TurnOrder;
import starcorp.common.types.OrderType;
import starcorp.common.types.PopulationClass;
import starcorp.server.turns.AOrderProcessor;

/**
 * starcorp.server.turns.orders.SetSalary
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 18 Sep 2007
 */
public class SetSalary extends AOrderProcessor {

	@Override
	public TurnError process(TurnOrder order) {
		TurnError error = null;
		int facilityId = order.getAsInt(0);
		String popClassKey = order.get(1);
		int salary = order.getAsInt(2);
		
		PopulationClass popClass = PopulationClass.getType(popClassKey);
		Facility facility = (Facility) entityStore.load(Facility.class, facilityId);
		
		if(facility == null) {
			error = new TurnError(TurnError.INVALID_FACILITY);
		}
		else if(popClass == null) {
			error = new TurnError(TurnError.INVALID_POP_CLASS);
		}
		else {
			Colony colony = facility.getColony();
			Workers workers = entityStore.getWorkers(facility, popClass);
			if(workers == null) {
				workers = new Workers();
				workers.setColony(colony);
				workers.setFacility(facility);
				workers.setHappiness(0.0);
				workers.setPopClass(popClass);
				workers.setSalary(0);
				entityStore.create(workers);
			}
			
			if(salary < workers.getSalary()) {
				Unemployed unemployed = entityStore.getUnemployed(colony, popClass);
				if(unemployed == null) {
					unemployed = new Unemployed();
					unemployed.setColony(colony);
					unemployed.setHappiness(0.0);
					unemployed.setPopClass(popClass);
					entityStore.create(unemployed);
				}
				long credits = entityStore.getCredits(workers);
				int qty = workers.getQuantity();
				unemployed.addPopulation(qty);
				workers.setQuantity(0);
				entityStore.update(workers);
				entityStore.update(unemployed);
				entityStore.transferCredits(workers, unemployed, credits, "");
			}
			
			workers.setSalary(salary);
			
			OrderReport report = new OrderReport(order);
			report.add(facility.getID());
			report.add(salary);
			order.setReport(report);
		}
		
		return error;
	}

	@Override
	public String getKey() {
		return OrderType.SET_SALARY;
	}

}
