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

import java.util.List;

import starcorp.client.turns.TurnError;
import starcorp.client.turns.TurnOrder;
import starcorp.common.entities.ActionReport;
import starcorp.common.entities.Colony;
import starcorp.common.entities.ColonyItem;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.DevelopmentGrant;
import starcorp.common.entities.Facility;
import starcorp.common.entities.FacilityLease;
import starcorp.common.types.AFacilityType;
import starcorp.common.types.BuildingModule;
import starcorp.common.types.GalacticDate;

/**
 * starcorp.server.turns.BuildFacility
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class BuildFacility extends AOrderProcessor {

	/* (non-Javadoc)
	 * @see starcorp.server.turns.AOrderProcessor#process(starcorp.client.turns.TurnOrder)
	 */
	@Override
	public TurnError process(TurnOrder order) {
		TurnError error = null;
		Corporation corp = order.getCorp();
		int colonyId = order.getAsInt(0);
		String facilityTypeKey = order.get(1);
		
		Colony colony = (Colony) entityStore.load(colonyId);
		AFacilityType facilityType = AFacilityType.getType(facilityTypeKey);
			
		if(colony == null) {
			error = new TurnError(TurnError.INVALID_COLONY);
		}
		else if(facilityType == null) {
			error = new TurnError(TurnError.INVALID_FACILITY_TYPE);
		}
		else {	
			Facility facility = new Facility();
			facility.setType(facilityType);
			facility.setColony(colony);
			facility.setOwner(corp);
			facility.setBuiltDate(GalacticDate.getCurrentDate());
			
			List<ColonyItem> buildingModules = entityStore.listItems(corp, colony, BuildingModule.class);
			
			FacilityLease lease = entityStore.getLease(colony, corp, facilityType, true);
			
			if(lease == null) {
				error = new TurnError(TurnError.NO_LEASE);
			}
			else {
				DevelopmentGrant grant = entityStore.getDevelopmentGrant(colony, facilityType, true);
				ActionReport report = corp.buildFacility(colony, facility, buildingModules, lease, grant);
			
				if(report.isSuccess()) {
					entityStore.save(facility);
				}
			}
		}
		return error;
	}

}
