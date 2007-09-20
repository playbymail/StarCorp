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
import starcorp.common.entities.Starship;
import starcorp.common.entities.StarshipDesign;
import starcorp.common.turns.OrderReport;
import starcorp.common.turns.TurnError;
import starcorp.common.turns.TurnOrder;
import starcorp.common.types.Items;
import starcorp.server.ServerConfiguration;

/**
 * starcorp.server.turns.BuildStarship
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class BuildStarship extends AOrderProcessor {

	/* (non-Javadoc)
	 * @see starcorp.server.turns.AOrderProcessor#process(starcorp.client.turns.TurnOrder)
	 */
	@Override
	public TurnError process(TurnOrder order) {
		TurnError error = null;
		Corporation corp = order.getCorp();
		int colonyId = order.getAsInt(0);
		int designId = order.getAsInt(1);
		String name = order.get(2);
		
		Colony colony = (Colony) entityStore.load(Colony.class, colonyId);
		StarshipDesign design = (StarshipDesign) entityStore.load(StarshipDesign.class, designId);
			
		if(colony == null) {
			error = new TurnError(TurnError.INVALID_COLONY);
		}
		else if(design == null) {
			error = new TurnError(TurnError.INVALID_SHIP_DESIGN);
		}
		else {	
			Starship ship = new Starship();
			ship.setBuiltDate(ServerConfiguration.getCurrentDate());
			ship.setDesign(design);
			ship.setPlanet(colony.getPlanet());
			ship.setName(name);
			ship.setOwner(corp);
			ship.setPlanetLocation(colony.getLocation());
			ship.setColony(colony);
			ship.setSystem(colony.getPlanet().getSystem());
			
			boolean hasNeededHulls = true;
			Iterator<Items> i = design.getHulls().iterator();
			
			while(i.hasNext()) {
				Items item = i.next();
				ColonyItem colonyItem = entityStore.getItem(colony, corp, item.getTypeClass());
				if(colonyItem == null || colonyItem.getItem().getQuantity() < item.getQuantity()) {
					error = new TurnError(TurnError.INSUFFICIENT_SHIP_HULLS);
					hasNeededHulls = false;
					break;
				}
			}
			
			if(hasNeededHulls) {
				i = design.getHulls().iterator();
				
				while(i.hasNext()) {
					Items item = i.next();
					ColonyItem colonyItem = entityStore.getItem(colony, corp, item.getTypeClass());
					if(colonyItem != null || !(colonyItem.getItem().getQuantity() < item.getQuantity())) {
						colonyItem.getItem().remove(item.getQuantity());
					}
				}
				
				entityStore.save(ship);
				
				OrderReport report = new OrderReport(order);
				report.add(name);
				report.add(ship.getID());
				report.add(design.getName());
				report.add(design.getID());
				report.add(colony.getName());
				report.add(colony.getID());
				order.setReport(report);
			}
		}
		return error;

	}

}
