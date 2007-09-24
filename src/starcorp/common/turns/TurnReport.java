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
package starcorp.common.turns;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import starcorp.common.entities.IEntity;
import starcorp.common.entities.MarketItem;
import starcorp.common.util.Util;

/**
 * starcorp.client.turns.TurnReport
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class TurnReport {

	private Turn turn;
	private List<IEntity> playerEntities = new ArrayList<IEntity>();
	private List<MarketItem> market;
	
	public TurnReport(Turn turn) {
		this.turn = turn;
	}
	
	public TurnReport(Element e) {
		this.turn = new Turn(e.element("turn"));
		for(Iterator<?> i = e.element("player-entities").elementIterator("entity"); i.hasNext();) {
			IEntity entity = Util.fromXML((Element)i.next());
			if(entity != null)
				playerEntities.add(entity);
		}
		for(Iterator<?> i = e.element("market").elementIterator("entity"); i.hasNext();) {
			MarketItem item = (MarketItem) Util.fromXML((Element)i.next());
			if(item != null)
				market.add(item);
		}
	}
	
	public Element toXML(Element parent) {
		Element root = parent.addElement("turn-report");
		turn.toXML(root);
		Element e = root.addElement("player-entities");
		e.addAttribute("size", String.valueOf(playerEntities.size()));
		for(Iterator<?> i = playerEntities.iterator(); i.hasNext();) {
			IEntity entity = (IEntity) i.next();
			entity.toFullXML(e);
		}
		e = root.addElement("market");
		e.addAttribute("size", String.valueOf(market.size()));
		if(market != null) {
			for(MarketItem item : market) {
				item.toBasicXML(e);
			}
		}
		return root;
	}
	
	public Turn getTurn() {
		return turn;
	}
	public void setTurn(Turn turn) {
		this.turn = turn;
	}
	public List<IEntity> getPlayerEntities() {
		return playerEntities;
	}
	public void addPlayerEntities(List<?> entities) {
		for(Object o : entities) {
			IEntity entity = (IEntity) o;
			playerEntities.add(entity);
		}
	}
	public void addPlayerEntity(IEntity entity) {
		playerEntities.add(entity);
	}

	public List<MarketItem> getMarket() {
		return market;
	}

	public void setMarket(List<MarketItem> market) {
		this.market = market;
	}
}
