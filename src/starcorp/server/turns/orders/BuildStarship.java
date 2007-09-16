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
import starcorp.common.entities.Starship;
import starcorp.common.entities.StarshipDesign;
import starcorp.common.types.GalacticDate;
import starcorp.common.types.StarshipHull;

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
		
		Colony colony = (Colony) entityStore.load(colonyId);
		StarshipDesign design = (StarshipDesign) entityStore.load(designId);
			
		if(colony == null) {
			error = new TurnError(TurnError.INVALID_COLONY);
		}
		else if(design == null) {
			error = new TurnError(TurnError.INVALID_SHIP_DESIGN);
		}
		else {	
			Starship ship = new Starship();
			ship.setBuiltDate(GalacticDate.getCurrentDate());
			ship.setDesign(design);
			ship.setPlanet(colony.getPlanet());
			ship.setName(name);
			ship.setOwner(corp);
			ship.setPlanetLocation(colony.getLocation());
			ship.setSystem(colony.getPlanet().getSystem());
			
			List<ColonyItem> shipHulls = entityStore.listItems(corp, colony, StarshipHull.class);
			
			ActionReport report = corp.buildShip(colony, ship, shipHulls);
			
			if(report.isSuccess()) {
				entityStore.save(ship);
			}
		}
		return error;

	}

}
