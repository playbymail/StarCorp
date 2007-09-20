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
import starcorp.common.entities.Corporation;
import starcorp.common.turns.Turn;
import starcorp.common.turns.TurnError;
import starcorp.common.turns.TurnOrder;
import starcorp.common.turns.TurnReport;
import starcorp.common.types.GalacticDate;
import starcorp.common.types.OrderType;
import starcorp.server.ServerConfiguration;
import starcorp.server.entitystore.IEntityStore;
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
			corp = register(turn.getCorporation());
			if(corp == null) {
				turn.add(TurnError.ERROR_AUTHORIZATION_FAILED);
			}
		}
		if(corp != null){
			turn.setCorporation(corp);
			GalacticDate lastTurn = corp.getLastTurnDate();
			GalacticDate currentDate = ServerConfiguration.getCurrentDate();
			if(lastTurn != null && !lastTurn.before(currentDate)) {
				turn.add(TurnError.ERROR_EARLY_TURN);
			}
			else {
				Iterator<TurnOrder> i = turn.getOrders().iterator();
				while(i.hasNext()) {
					TurnOrder order = i.next();
					order.setCorp(corp);
					TurnError error = process(order);
					if(error != null) {
						turn.add(error);
					}
				}
				report.addPlayerEntities(entityStore.listColonistGrants(corp, true));
				report.addPlayerEntities(entityStore.listDesigns(corp));
				report.addPlayerEntities(entityStore.listDevelopmentGrants(corp, true));
				report.addPlayerEntities(entityStore.listFacilities(corp));
				report.addPlayerEntities(entityStore.listItems(corp));
				report.addPlayerEntities(entityStore.listLeases(corp, true));
				report.addPlayerEntities(entityStore.listMarket(1));
				report.addPlayerEntities(entityStore.listShips(corp));
			}
		}
		return report;
	}
	
	private Corporation register(Corporation corp) {
		Corporation existing = entityStore.getCorporation(corp.getPlayerEmail());
		if(existing == null) {
			corp.add(ServerConfiguration.SETUP_INITIAL_CREDITS,ServerConfiguration.getCurrentDate(),ServerConfiguration.SETUP_DESCRIPTION);
			corp.setFoundedDate(ServerConfiguration.getCurrentDate());
			corp.setLastTurnDate(ServerConfiguration.getCurrentDate());
			entityStore.save(corp);
			return corp;
		}
		else {
			return null;
		}
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
