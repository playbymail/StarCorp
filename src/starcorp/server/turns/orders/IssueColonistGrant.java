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

import starcorp.common.entities.ColonistGrant;
import starcorp.common.entities.Colony;
import starcorp.common.entities.Corporation;
import starcorp.common.turns.OrderReport;
import starcorp.common.turns.TurnError;
import starcorp.common.turns.TurnOrder;
import starcorp.common.types.GalacticDate;
import starcorp.common.types.PopulationClass;

/**
 * starcorp.server.turns.IssueColonistGrant
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class IssueColonistGrant extends AOrderProcessor {

	@Override
	public TurnError process(TurnOrder order) {
		TurnError error = null;
		Corporation corp = order.getCorp(); 
		int colonyId = order.getAsInt(0);
		String popClassType = order.get(1);
		int credit = order.getAsInt(2);
		
		Colony colony = (Colony) entityStore.load(colonyId);
		PopulationClass popClass = PopulationClass.getType(popClassType);
		
		if(colony == null) {
			error = new TurnError(TurnError.INVALID_COLONY);
		}
		else if(popClass == null) {
			error = new TurnError(TurnError.INVALID_POP_CLASS);
		}
		else if(!colony.getGovernment().equals(corp)){
			error = new TurnError(TurnError.INVALID_COLONY);
		}
		else {
			ColonistGrant grant = new ColonistGrant();
			grant.setAvailable(true);
			grant.setColony(colony);
			grant.setCredits(credit);
			grant.setIssuedDate(GalacticDate.getCurrentDate());
			grant.setPopClass(popClass);
			
			entityStore.save(grant);
			
			OrderReport report = new OrderReport(order);
			report.add(popClass.getName());
			report.add(credit);
			report.add(colony.getName());
			report.add(colony.getID());
			order.setReport(report);
			
		}
		return error;
	}

}
