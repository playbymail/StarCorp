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

import starcorp.common.entities.Facility;
import starcorp.common.turns.OrderReport;
import starcorp.common.turns.TurnError;
import starcorp.common.turns.TurnOrder;
import starcorp.common.types.AFactoryItem;
import starcorp.common.types.AItemType;
import starcorp.common.types.Items;

/**
 * starcorp.server.turns.FactoryBuild
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class FactoryBuild extends AOrderProcessor {

	@Override
	public TurnError process(TurnOrder order) {
		TurnError error = null;
		int factoryId = order.getAsInt(0);
		String itemTypeKey = order.get(1);
		int quantity = order.getAsInt(2);
		
		Facility factory = (Facility) entityStore.load(factoryId);
		AItemType type = AItemType.getType(itemTypeKey);
		if(factory == null) {
			error = new TurnError(TurnError.INVALID_FACILITY);
		}
		else if(type == null || !(type instanceof AFactoryItem)) {
			error = new TurnError(TurnError.INVALID_ITEM);
		}
		else {
			Items item = new Items();
			item.setQuantity(quantity);
			item.setTypeClass(type);
			if(factory.queueItem(item)) {
				OrderReport report = new OrderReport(order);
				report.add(type.getName());
				report.add(quantity);
				report.add(factory.getID());
				order.setReport(report);
			}
			else {
				error = new TurnError(TurnError.INVALID_ITEM);
			}
		}
		return error;
	}

}
