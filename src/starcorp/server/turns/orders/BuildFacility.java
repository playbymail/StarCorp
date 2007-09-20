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

import java.util.Iterator;
import starcorp.common.entities.Colony;
import starcorp.common.entities.ColonyItem;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.DevelopmentGrant;
import starcorp.common.entities.Facility;
import starcorp.common.entities.FacilityLease;
import starcorp.common.turns.OrderReport;
import starcorp.common.turns.TurnError;
import starcorp.common.turns.TurnOrder;
import starcorp.common.types.AFacilityType;
import starcorp.common.types.CashTransaction;
import starcorp.common.types.ColonyHub;
import starcorp.common.types.Items;
import starcorp.common.types.OrbitalDock;
import starcorp.common.types.OrderType;
import starcorp.server.ServerConfiguration;
import starcorp.server.turns.AOrderProcessor;

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
		
		Colony colony = (Colony) entityStore.load(Colony.class, colonyId);
		AFacilityType facilityType = AFacilityType.getType(facilityTypeKey);
			
		if(colony == null) {
			error = new TurnError(TurnError.INVALID_COLONY);
		}
		else if(facilityType == null) {
			error = new TurnError(TurnError.INVALID_FACILITY_TYPE);
		}
		else if(facilityType instanceof ColonyHub) {
			error = new TurnError(TurnError.INVALID_FACILITY_TYPE);
		}
		else {
			if(facilityType instanceof OrbitalDock) {
				if(entityStore.listFacilities(colony, OrbitalDock.class).size() > 0) {
					return new TurnError(TurnError.INVALID_FACILITY_TYPE);
				}
			}
			Facility facility = new Facility();
			facility.setTypeClass(facilityType);
			facility.setColony(colony);
			facility.setOwner(corp);
			facility.setBuiltDate(ServerConfiguration.getCurrentDate());
			facility.setOpen(true);
			
			FacilityLease lease = entityStore.getLease(colony, corp, facilityType, true);
			
			if(lease == null) {
				error = new TurnError(TurnError.NO_LEASE);
			}
			else {
				DevelopmentGrant grant = entityStore.getDevelopmentGrant(colony, facilityType, true);
				
				boolean hasNeededModules = true;
				Iterator<Items> i = facilityType.getBuildingRequirement().iterator();
				while(i.hasNext()) {
					Items item = i.next();
					ColonyItem colonyItem =  entityStore.getItem(colony, corp, item.getTypeClass());
					if(colonyItem == null || colonyItem.getItem().getQuantity() < item.getQuantity()) {
						error = new TurnError(TurnError.INSUFFICIENT_BUILDING_MODULES);
						hasNeededModules = false;
						break;
					}
				}
				
				if(hasNeededModules) {
					i = facilityType.getBuildingRequirement().iterator();
					
					while(i.hasNext()) {
						Items item = i.next();
						ColonyItem colonyItem = entityStore.getItem(colony, corp, item.getTypeClass());
						if(colonyItem != null || !(colonyItem.getItem().getQuantity() < item.getQuantity())) {
							colonyItem.getItem().remove(item.getQuantity());
						}
					}
					
					lease.setUsed(true);
					lease.setUsedDate(ServerConfiguration.getCurrentDate());
					
					if(grant != null) {
						Object[] args = {facilityType.getName(), colony.getName(), String.valueOf(colony.getID())};
						String govtDesc = CashTransaction.getDescription(CashTransaction.GRANT_PAID, args);
						String corpDesc = CashTransaction.getDescription(CashTransaction.GRANT_RECEIVED, args);
						grant.getColony().getGovernment().remove(grant.getGrant(), ServerConfiguration.getCurrentDate(), govtDesc);
						corp.add(grant.getGrant(), ServerConfiguration.getCurrentDate(), corpDesc);
					}
					
					entityStore.save(facility);
					
					OrderReport report = new OrderReport(order);
					report.add(facilityType.getName());
					report.add(facility.getID());
					report.add(colony.getName());
					report.add(colony.getID());
					order.setReport(report);
				}
			}
		}
		return error;
	}

	@Override
	public String getKey() {
		return OrderType.BUILD_FACILITY;
	}

}
