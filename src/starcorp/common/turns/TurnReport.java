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

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import starcorp.common.entities.AGovernmentLaw;
import starcorp.common.entities.Colony;
import starcorp.common.entities.ColonyItem;
import starcorp.common.entities.Facility;
import starcorp.common.entities.IEntity;
import starcorp.common.entities.MarketItem;
import starcorp.common.entities.Planet;
import starcorp.common.entities.StarSystem;
import starcorp.common.entities.StarSystemEntity;
import starcorp.common.entities.Starship;
import starcorp.common.entities.StarshipDesign;
import starcorp.common.entities.StellarAnomoly;
import starcorp.common.types.AFacilityType;
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
	private List<AGovernmentLaw> laws;
	private List<ColonyItem> items;
	private List<StarSystem> knownSystems;
	
	public TurnReport(Turn turn) {
		this.turn = turn;
	}
	
	public TurnReport(InputStream is) throws DocumentException {
		SAXReader sax = new SAXReader();
		Document doc = sax.read(is);
		readXML(doc.getRootElement().element("turn-report"));
	}
	
	public void write(Writer writer) throws IOException {
		Document doc = DocumentHelper.createDocument();
		toXML(doc.addElement("starcorp"));
		// TODO switch to compact format to save space after debugging
		// OutputFormat format = OutputFormat.createCompactFormat();
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter xmlWriter = new XMLWriter(
			writer, format
		);
		
		xmlWriter.write(doc);
		xmlWriter.close();
	}
	
	public TurnReport(Element e) {
		readXML(e);
	}
	
	public void readXML(Element e) {
		this.turn = new Turn(e.element("turn"));
		for(Iterator<?> i = e.element("player-entities").elementIterator("entity"); i.hasNext();) {
			IEntity entity = Util.fromXML((Element)i.next());
			if(entity != null)
				playerEntities.add(entity);
		}
		market = new ArrayList<MarketItem>();
		Element eMarket = e.element("market");
		if(eMarket != null) {
			for(Iterator<?> i = eMarket.elementIterator("entity"); i.hasNext();) {
				MarketItem item = (MarketItem) Util.fromXML((Element)i.next());
				if(item != null)
					market.add(item);
			}
		}
		laws = new ArrayList<AGovernmentLaw>();
		Element eLaws = e.element("laws");
		if(eLaws != null) {
			for(Iterator<?> i = eLaws.elementIterator("entity"); i.hasNext();) {
				AGovernmentLaw item = (AGovernmentLaw) Util.fromXML((Element)i.next());
				if(item != null)
					laws.add(item);
			}
		}
		items = new ArrayList<ColonyItem>();
		Element eItems = e.element("items");
		if(eItems != null) {
			for(Iterator<?> i = eItems.elementIterator("entity"); i.hasNext();) {
				ColonyItem item = (ColonyItem) Util.fromXML((Element)i.next());
				if(item != null)
					items.add(item);
			}
		}
		knownSystems = new ArrayList<StarSystem>();
		Element eSystems = e.element("systems");
		if(eSystems != null) {
			for(Iterator<?> i = eSystems.elementIterator("entity"); i.hasNext();) {
				StarSystem item = (StarSystem) Util.fromXML((Element)i.next());
				if(item != null)
					knownSystems.add(item);
			}
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
		if(market != null) {
			e.addAttribute("size", String.valueOf(market.size()));
			for(MarketItem item : market) {
				item.toBasicXML(e);
			}
		}
		e = root.addElement("laws");
		if(laws != null) {
			e.addAttribute("size", String.valueOf(laws.size()));
			for(AGovernmentLaw item : laws) {
				item.toBasicXML(e);
			}
		}
		e = root.addElement("items");
		if(laws != null) {
			e.addAttribute("size", String.valueOf(items.size()));
			for(ColonyItem item : items) {
				item.toBasicXML(e);
			}
		}
		e = root.addElement("systems");
		if(knownSystems != null) {
			e.addAttribute("size", String.valueOf(knownSystems.size()));
			for(StarSystem system : knownSystems) {
				system.toBasicXML(e);
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
	
	public int countPlayerStarships() {
		return getPlayerStarships().size();
	}
	
	public int countPlayerFacilities() {
		int count = 0;
		for(Object o : playerEntities) {
			if(o instanceof Facility) {
				count++;
			}
		}
		return count;
	}
	
	public List<StellarAnomoly> getScannedAnomolies() {
		List<StellarAnomoly> list = new ArrayList<StellarAnomoly>();
		for(Object o : turn.getScannedEntities(StellarAnomoly.class)) {
			list.add((StellarAnomoly)o);
		}
		return list;
	}
	
	public List<Colony> getKnownColonies() {
		List<Colony> colonies = new ArrayList<Colony>();
		colonies.addAll(getPlayerFacilitiesByColony().keySet());
		for(Object o : turn.getScannedEntities(Colony.class)) {
			colonies.add((Colony)o);
		}
		return colonies;
	}
	
	public List<Planet> getScannedPlanets() {
		List<Planet> entities = new ArrayList<Planet>();
		for(Object o :turn.getScannedEntities(Planet.class)) {
			Planet p = (Planet)o;
			entities.add(p);
		}
		return entities;
	}

	public List<StarSystemEntity> getScannedAsteroids() {
		List<StarSystemEntity> entities = new ArrayList<StarSystemEntity>();
		for(Object o :turn.getScannedEntities(StarSystemEntity.class)) {
			StarSystemEntity entity = (StarSystemEntity)o;
			if(entity.isAsteroid()) {
				entities.add(entity);
			}
		}
		return entities;
	}
	
	public List<StarSystemEntity> getScannedGasFields() {
		List<StarSystemEntity> entities = new ArrayList<StarSystemEntity>();
		for(Object o :turn.getScannedEntities(StarSystemEntity.class)) {
			StarSystemEntity entity = (StarSystemEntity)o;
			if(entity.isGasfield()) {
				entities.add(entity);
			}
		}
		return entities;
	}
	
	public List<Starship> getPlayerStarships(StarshipDesign design) {
		return getPlayerStarshipsByDesign().get(design);
	}
	
	public Map<StarshipDesign,List<Starship>> getPlayerStarshipsByDesign() {
		Map<StarshipDesign,List<Starship>> map = new TreeMap<StarshipDesign, List<Starship>>();
		
		for(Object o : playerEntities) {
			if(o instanceof Starship) {
				Starship ship = (Starship) o;
				List<Starship> list = map.get(ship.getDesign());
				if(list == null) {
					list = new ArrayList<Starship>();
					map.put(ship.getDesign(),list);
				}
				list.add(ship);
			}
		}
		return map;
	}
	
	public List<Starship> getPlayerStarships() {
		List<Starship> list = new ArrayList<Starship>();
		for(Object o : playerEntities) {
			if(o instanceof Starship)
				list.add((Starship)o);
		}
		return list;
	}
	
	public Map<Colony,List<Facility>> getPlayerFacilitiesByColony() {
		Map<Colony, List<Facility>> map = new TreeMap<Colony, List<Facility>>(); 
		for(Object o : playerEntities) {
			if(o instanceof Facility) {
				Facility f = (Facility) o;
				List<Facility> list = map.get(f.getColony());
				if(list == null) {
					list = new ArrayList<Facility>();
					map.put(f.getColony(), list);
				}
				list.add(f);
			}
		}
		return map;
	}
	
	public List<Facility> getPlayerFacilities() {
		List<Facility> list = new ArrayList<Facility>();
		for(Object o : playerEntities) {
			if(o instanceof Facility) {
				list.add((Facility)o);
			}
		}
		return list;
	}

	public List<Facility> getPlayerFacilitiesByType(Class clazz) {
		List<Facility> list = new ArrayList<Facility>();
		for(Object o : playerEntities) {
			if(o instanceof Facility) {
				Facility f = (Facility) o;
				if(f.getTypeClass().getClass().equals(clazz)) {
					list.add(f);
				}
			}
		}
		return list;
	}

	public List<StarshipDesign> getPlayerDesigns() {
		List<StarshipDesign> list = new ArrayList<StarshipDesign>();
		for(Object o : playerEntities) {
			if(o instanceof StarshipDesign)
				list.add((StarshipDesign)o);
		}
		return list;
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

	public List<AGovernmentLaw> getLaws() {
		return laws;
	}

	public void setLaws(List<AGovernmentLaw> laws) {
		this.laws = laws;
	}

	public List<ColonyItem> getItems() {
		return items;
	}

	public void setItems(List<ColonyItem> items) {
		this.items = items;
	}

	public List<StarSystem> getKnownSystems() {
		return knownSystems;
	}

	public void setKnownSystems(List<StarSystem> knownSystems) {
		this.knownSystems = knownSystems;
	}
}
