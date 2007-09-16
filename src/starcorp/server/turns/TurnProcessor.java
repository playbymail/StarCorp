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
package starcorp.server.turns;

import java.util.Iterator;
import starcorp.client.turns.Turn;
import starcorp.client.turns.TurnError;
import starcorp.client.turns.TurnOrder;
import starcorp.common.entities.Corporation;
import starcorp.common.types.GalacticDate;
import starcorp.common.types.OrderType;
import starcorp.server.entitystore.IEntityStore;
import starcorp.server.reports.TurnReport;
import starcorp.server.turns.orders.AOrderProcessor;

/**
 * starcorp.server.turns.TurnProcessor
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class TurnProcessor {

	private IEntityStore entityStore;
	
	public TurnProcessor(IEntityStore entityStore) {
		this.entityStore = entityStore;
	}
	
	public TurnReport process(Turn turn) {
		Corporation corp = authorize(turn.getCorporation());
		TurnReport report = new TurnReport(turn);
		if(corp == null) {
			turn.add(TurnError.ERROR_AUTHORIZATION_FAILED);
		}
		else {
			report.setTo(corp);
			turn.setCorporation(corp);
			GalacticDate lastTurn = corp.getLastTurnDate();
			GalacticDate currentDate = GalacticDate.getCurrentDate();
			if(lastTurn != null && !lastTurn.before(currentDate)) {
				turn.add(TurnError.ERROR_EARLY_TURN);
			}
			else {
				Iterator<TurnOrder> i = turn.getOrders().iterator();
				while(i.hasNext()) {
					TurnOrder order = i.next();
					TurnError error = process(order);
					if(error != null) {
						turn.add(error);
					}
				}
				report.setColonistGrants(entityStore.listColonistGrants(corp, true));
				report.setDesigns(entityStore.listDesigns(corp));
				report.setDevelopmentGrants(entityStore.listDevelopmentGrants(corp, true));
				report.setFacilities(entityStore.listFacilities(corp));
				report.setItems(entityStore.listItems(corp));
				report.setLeases(entityStore.listLeases(corp, true));
				report.setMarkets(entityStore.listMarket(1));
				report.setStarships(entityStore.listShips(corp));
			}
		}
		return report;
	}
	
	private Corporation authorize(Corporation corp) {
		return entityStore.getCorporation(corp.getPlayerEmail(), corp.getPlayerPassword());
	}
	
	private TurnError process(TurnOrder order) {
		TurnError error = null;
		OrderType type = order.getType();
		AOrderProcessor processor = AOrderProcessor.getProcessor(type.getKey());
		if(processor == null) {
			error = new TurnError(TurnError.INVALID_ORDER_TYPE,order);
		}
		else {
			processor.setEntityStore(entityStore);
			error = processor.process(order);
		}
		return error;
		
	}
}
