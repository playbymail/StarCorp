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

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
import starcorp.common.entities.Corporation;
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
import starcorp.common.types.CoordinatesPolar;
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
	private Set<StarSystem> knownSystems;
	
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
		knownSystems = new TreeSet<StarSystem>();
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
	
	public Set<StellarAnomoly> getScannedAnomolies() {
		Set<StellarAnomoly> list = new TreeSet<StellarAnomoly>();
		for(Object o : turn.getScannedEntities(StellarAnomoly.class)) {
			list.add((StellarAnomoly)o);
		}
		return list;
	}
	
	public Set<StellarAnomoly> getScannedAnomolies(CoordinatesPolar location) {
		Set<StellarAnomoly> list = new TreeSet<StellarAnomoly>();
		for(Object o : turn.getScannedEntities(StellarAnomoly.class)) {
			StellarAnomoly anomoly = (StellarAnomoly)o;
			if(anomoly.getLocation().equals(location))
				list.add(anomoly);
		}
		return list;
	}
	
	public Set<Colony> getKnownColonies() {
		Set<Colony> colonies = new TreeSet<Colony>();
		colonies.addAll(getPlayerFacilitiesByColony().keySet());
		colonies.addAll(getMarketByColony().keySet());
		for(Object o : turn.getScannedEntities(Colony.class)) {
			colonies.add((Colony)o);
		}
		for(Starship ship : getPlayerStarships()) {
			if(ship.getColony() != null)
				colonies.add(ship.getColony());
		}
		return colonies;
	}
	
	public Set<Colony> getKnownColonies(Planet planet) {
		Set<Colony> colonies = new TreeSet<Colony>();
		for(Colony colony : getKnownColonies()) {
			if(colony.getPlanetID() == planet.getID())
				colonies.add(colony);
		}
		return colonies;
	}
	
	public Set<Planet> getScannedPlanets() {
		Set<Planet> entities = new TreeSet<Planet>();
		for(Object o :turn.getScannedEntities(Planet.class)) {
			Planet p = (Planet)o;
			entities.add(p);
		}
		return entities;
	}
	
	public Set<Planet> getScannedPlanets(long systemID, CoordinatesPolar location) {
		Set<Planet> entities = new TreeSet<Planet>();
		for(Object o :turn.getScannedEntities(Planet.class)) {
			Planet p = (Planet)o;
			if(p.getSystemID() == systemID && p.getLocation().equals(location))
				entities.add(p);
		}
		return entities;
	}

	public Set<Planet> getScannedPlanets(long systemID) {
		Set<Planet> entities = new TreeSet<Planet>();
		for(Object o :turn.getScannedEntities(Planet.class)) {
			Planet p = (Planet)o;
			if(p.getSystemID() == systemID)
				entities.add(p);
		}
		return entities;
	}

	public Set<StarSystemEntity> getScannedEntitiesExcludeShips(long systemID) {
		Set<StarSystemEntity> entities = new TreeSet<StarSystemEntity>();
		for(Object o :turn.getScannedEntities(StarSystemEntity.class)) {
			StarSystemEntity p = (StarSystemEntity)o;
			if(!(p.getClass().equals(Starship.class))) {
				if(p.getSystemID() == systemID)
					entities.add(p);
			}
		}
		return entities;
	}

	public Set<StarSystemEntity> getScannedAsteroids() {
		Set<StarSystemEntity> entities = new TreeSet<StarSystemEntity>();
		for(Object o :turn.getScannedEntities(StarSystemEntity.class)) {
			StarSystemEntity entity = (StarSystemEntity)o;
			if(entity.isAsteroid()) {
				entities.add(entity);
			}
		}
		return entities;
	}
	
	public Set<StarSystemEntity> getScannedAsteroids(CoordinatesPolar location) {
		Set<StarSystemEntity> entities = new TreeSet<StarSystemEntity>();
		for(Object o :turn.getScannedEntities(StarSystemEntity.class)) {
			StarSystemEntity entity = (StarSystemEntity)o;
			if(entity.isAsteroid() && entity.getLocation().equals(location)) {
				entities.add(entity);
			}
		}
		return entities;
	}
	
	public Set<StarSystemEntity> getScannedGasFields() {
		Set<StarSystemEntity> entities = new TreeSet<StarSystemEntity>();
		for(Object o :turn.getScannedEntities(StarSystemEntity.class)) {
			StarSystemEntity entity = (StarSystemEntity)o;
			if(entity.isGasfield()) {
				entities.add(entity);
			}
		}
		return entities;
	}
	
	public Set<StarSystemEntity> getScannedGasFields(CoordinatesPolar location) {
		Set<StarSystemEntity> entities = new TreeSet<StarSystemEntity>();
		for(Object o :turn.getScannedEntities(StarSystemEntity.class)) {
			StarSystemEntity entity = (StarSystemEntity)o;
			if(entity.isGasfield() && entity.getLocation().equals(location)) {
				entities.add(entity);
			}
		}
		return entities;
	}
	
	public Set<Starship> getPlayerStarships(StarshipDesign design) {
		return getPlayerStarshipsByDesign().get(design);
	}
	
	public Map<StarshipDesign,Set<Starship>> getPlayerStarshipsByDesign() {
		Map<StarshipDesign,Set<Starship>> map = new TreeMap<StarshipDesign, Set<Starship>>();
		
		for(Object o : playerEntities) {
			if(o instanceof Starship) {
				Starship ship = (Starship) o;
				Set<Starship> list = map.get(ship.getDesign());
				if(list == null) {
					list = new TreeSet<Starship>();
					map.put(ship.getDesign(),list);
				}
				list.add(ship);
			}
		}
		return map;
	}
	
	public Set<Starship> getPlayerOrbitingrStarships(Planet planet) {
		Set<Starship> list = new TreeSet<Starship>();
		for(Starship ship : getPlayerStarships() ) {
			if(ship.isOrbiting(planet))
				list.add(ship);
		}
		return list;
	}
	
	public Set<Starship> getPlayerStarships(Colony colony) {
		Set<Starship> list = new TreeSet<Starship>();
		for(Starship ship : getPlayerStarships() ) {
			if(colony.equals(ship.getColony()))
				list.add(ship);
		}
		return list;
	}
	
	public Set<Starship> getPlayerStarshipsInOrOrbitingColony(Colony colony) {
		Set<Starship> list = new TreeSet<Starship>();
		for(Starship ship : getPlayerStarships() ) {
			if(colony.equals(ship.getColony()) || ship.isOrbiting(colony.getID()))
				list.add(ship);
		}
		return list;
	}
	
	public Set<Starship> getPlayerStarships() {
		Set<Starship> list = new TreeSet<Starship>();
		for(Object o : playerEntities) {
			if(o instanceof Starship)
				list.add((Starship)o);
		}
		return list;
	}
	
	public Map<Colony,Set<Facility>> getPlayerFacilitiesByColony() {
		Map<Colony, Set<Facility>> map = new TreeMap<Colony, Set<Facility>>(); 
		for(Object o : playerEntities) {
			if(o instanceof Facility) {
				Facility f = (Facility) o;
				Set<Facility> list = map.get(f.getColony());
				if(list == null) {
					list = new TreeSet<Facility>();
					map.put(f.getColony(), list);
				}
				list.add(f);
			}
		}
		return map;
	}
	
	public Map<AFacilityType,Map<Colony,Set<Facility>>> getPlayerFacilitiesByTypeAndColony() {
		Map<AFacilityType,Map<Colony,Set<Facility>>> facMap = new TreeMap<AFacilityType, Map<Colony,Set<Facility>>>();
		 
		for(Object o : playerEntities) {
			if(o instanceof Facility) {
				Facility f = (Facility) o;
				Map<Colony, Set<Facility>> map = facMap.get(f.getTypeClass());
				if(map == null) {
					map = new TreeMap<Colony, Set<Facility>>();
					facMap.put(f.getTypeClass(), map);
				}
				Set<Facility> list = map.get(f.getColony());
				if(list == null) {
					list = new TreeSet<Facility>();
					map.put(f.getColony(), list);
				}
				list.add(f);
			}
		}
		return facMap;
	}
	
	public Set<Facility> getPlayerFacilities() {
		Set<Facility> list = new TreeSet<Facility>();
		for(Object o : playerEntities) {
			if(o instanceof Facility) {
				list.add((Facility)o);
			}
		}
		return list;
	}

	public Set<Facility> getPlayerFacilitiesByType(Class clazz) {
		Set<Facility> list = new TreeSet<Facility>();
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

	public Set<StarshipDesign> getPlayerDesigns() {
		Set<StarshipDesign> list = new TreeSet<StarshipDesign>();
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
	
	public Map<Colony, Set<MarketItem>> getMarketByColony() {
		Map<Colony, Set<MarketItem>> map = new TreeMap<Colony, Set<MarketItem>>();
		for(MarketItem item : getMarket()) {
			Set<MarketItem> list = map.get(item.getColony());
			if(list == null) {
				list = new TreeSet<MarketItem>();
				map.put(item.getColony(),list);
			}
			list.add(item);
		}
		return map;
	}
	
	public Map<Corporation, Set<MarketItem>> getMarketBySeller() {
		Map<Corporation, Set<MarketItem>> map = new TreeMap<Corporation, Set<MarketItem>>();
		for(MarketItem item : getMarket()) {
			Set<MarketItem> list = map.get(item.getSeller());
			if(list == null) {
				list = new TreeSet<MarketItem>();
				map.put(item.getSeller(),list);
			}
			list.add(item);
		}
		return map;
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

	public Set<StarSystem> getKnownSystems() {
		return knownSystems;
	}

	public void setKnownSystems(Set<StarSystem> knownSystems) {
		this.knownSystems = knownSystems;
	}
}
