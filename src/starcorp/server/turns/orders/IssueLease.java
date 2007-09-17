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
import starcorp.common.entities.Corporation;
import starcorp.common.entities.FacilityLease;
import starcorp.common.types.AFacilityType;
import starcorp.common.types.GalacticDate;

/**
 * starcorp.server.turns.IssueLease
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class IssueLease extends AOrderProcessor {

	@Override
	public TurnError process(TurnOrder order) {
		TurnError error = null;
		Corporation corp = order.getCorp(); 
		int colonyId = order.getAsInt(0);
		String facilityType = order.get(1);
		int price = order.getAsInt(2);
		int licenseeId = order.getAsInt(3);
		
		Colony colony = (Colony) entityStore.load(colonyId);
		AFacilityType type = AFacilityType.getType(facilityType);
		Corporation licensee = (Corporation) entityStore.load(licenseeId);
		
		if(colony == null) {
			error = new TurnError(TurnError.INVALID_COLONY);
		}
		else if(type == null) {
			error = new TurnError(TurnError.INVALID_FACILITY_TYPE);
		}
		else if(!colony.getGovernment().equals(corp)){
			error = new TurnError(TurnError.INVALID_COLONY);
		}
		else {
			FacilityLease lease = new FacilityLease();
			lease.setColony(colony);
			lease.setIssuedDate(GalacticDate.getCurrentDate());
			lease.setLicensee(licensee);
			lease.setPrice(price);
			lease.setType(type);
			
			entityStore.save(lease);
			
			OrderReport report = new OrderReport(order);
			report.add(colony.getName());
			report.add(colony.getID());
			report.add(type.getName());
			report.add(price);
			order.setReport(report);
			
		}
		return error;
	}

}
