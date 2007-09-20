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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.StarshipDesign;
import starcorp.common.turns.OrderReport;
import starcorp.common.turns.TurnError;
import starcorp.common.turns.TurnOrder;
import starcorp.common.types.AItemType;
import starcorp.common.types.OrderType;
import starcorp.common.types.StarshipHulls;
import starcorp.server.ServerConfiguration;
import starcorp.server.turns.AOrderProcessor;

/**
 * starcorp.server.turns.DesignShip
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class DesignShip extends AOrderProcessor {

	@Override
	public TurnError process(TurnOrder order) {
		TurnError error = null;
		Corporation corp = order.getCorp();
		int max = order.size();
		String name = order.get(0);
		List<StarshipHulls> hullTypes = new ArrayList<StarshipHulls>(); 
		for(int i = 1; i < max; i++) {
			String s = order.get(i);
			AItemType type = AItemType.getType(s);
			if(type instanceof StarshipHulls) {
				hullTypes.add((StarshipHulls)type);
			}
		}
		
		if(name == null || hullTypes.size() < 1) {
			error = new TurnError(TurnError.INVALID_SHIP_DESIGN);
		}
		else {
			StarshipDesign design = new StarshipDesign();
			design.setDesignDate(ServerConfiguration.getCurrentDate());
			design.setName(name);
			design.setOwner(corp);
			Iterator<StarshipHulls> i = hullTypes.iterator();
			while(i.hasNext()) {
				StarshipHulls hull = i.next();
				design.addHulls(hull, 1);
			}
			
			if(design.isValid()) {
				entityStore.save(design);
				
				OrderReport report = new OrderReport(order);
				report.add(name);
				report.add(design.getID());
				order.setReport(report);
			}
			else {
				error = new TurnError(TurnError.INVALID_SHIP_DESIGN);
			}
			
		}
		
		return error;
	}
	@Override
	public String getKey() {
		return OrderType.DESIGN_SHIP;
	}

}
