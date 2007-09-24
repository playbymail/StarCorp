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
import starcorp.common.util.Util;

/**
 * starcorp.client.turns.TurnReport
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class TurnReport {

	private Turn turn;
	@SuppressWarnings("unchecked")
	private List playerEntities = new ArrayList();
	
	public TurnReport(Turn turn) {
		this.turn = turn;
	}
	
	@SuppressWarnings("unchecked")
	public TurnReport(Element e) {
		this.turn = new Turn(e.element("turn"));
		for(Iterator<?> i = e.element("player-entities").elementIterator("entity"); i.hasNext();) {
			IEntity entity = Util.fromXML((Element)i.next());
			if(entity != null)
				playerEntities.add(entity);
		}
	}
	
	public Element toXML(Element parent) {
		Element root = parent.addElement("turn-report");
		turn.toXML(root);
		Element e = root.addElement("player-entities");
		for(Iterator<?> i = playerEntities.iterator(); i.hasNext();) {
			IEntity entity = (IEntity) i.next();
			entity.toFullXML(e);
		}
		return root;
	}
	
	public Turn getTurn() {
		return turn;
	}
	public void setTurn(Turn turn) {
		this.turn = turn;
	}
	public List<?> getPlayerEntities() {
		return playerEntities;
	}
	@SuppressWarnings("unchecked")
	public void addPlayerEntities(List entities) {
		playerEntities.addAll(entities);
	}
	@SuppressWarnings("unchecked")
	public void addPlayerEntity(IEntity entity) {
		playerEntities.add(entity);
	}
}
