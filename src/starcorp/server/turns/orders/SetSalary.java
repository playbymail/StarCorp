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

import starcorp.client.turns.OrderReport;
import starcorp.client.turns.TurnError;
import starcorp.client.turns.TurnOrder;
import starcorp.common.entities.Colony;
import starcorp.common.entities.Facility;
import starcorp.common.entities.Unemployed;
import starcorp.common.entities.Workers;
import starcorp.common.types.Population;
import starcorp.common.types.PopulationClass;

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
		Facility facility = (Facility) entityStore.load(facilityId);
		
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
				workers.setCash(0);
				workers.setColony(colony);
				workers.setFacility(facility);
				workers.setHappiness(0.0);
				workers.setPopulation(new Population(popClass));
				workers.setSalary(0);
				entityStore.save(workers);
			}
			
			if(salary < workers.getSalary()) {
				Unemployed unemployed = entityStore.getUnemployed(colony, popClass);
				if(unemployed == null) {
					unemployed = new Unemployed();
					unemployed.setCash(0);
					unemployed.setColony(colony);
					unemployed.setHappiness(0.0);
					unemployed.setPopulation(new Population(popClass));
					entityStore.save(unemployed);
				}
				unemployed.addPopulation(workers.getQuantity());
				unemployed.addCash(workers.getCash());
				workers.getPopulation().setQuantity(0);
				workers.setCash(0);
			}
			
			workers.setSalary(salary);
			
			OrderReport report = new OrderReport(order);
			report.add(facility.getID());
			report.add(salary);
			order.setReport(report);
		}
		
		return error;
	}

}
