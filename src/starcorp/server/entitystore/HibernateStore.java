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
package starcorp.server.entitystore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import starcorp.common.entities.ABaseEntity;
import starcorp.common.entities.AStarSystemEntity;
import starcorp.common.entities.ColonistGrant;
import starcorp.common.entities.Colony;
import starcorp.common.entities.ColonyItem;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.DevelopmentGrant;
import starcorp.common.entities.Facility;
import starcorp.common.entities.FacilityLease;
import starcorp.common.entities.Planet;
import starcorp.common.entities.StarSystem;
import starcorp.common.entities.Unemployed;
import starcorp.common.entities.Workers;
import starcorp.common.types.AFacilityType;
import starcorp.common.types.AItemType;
import starcorp.common.types.Coordinates2D;
import starcorp.common.types.Coordinates3D;
import starcorp.common.types.CoordinatesPolar;
import starcorp.common.types.PopulationClass;

/**
 * starcorp.server.entitystore.HibernateStore
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 19 Sep 2007
 */
public class HibernateStore implements IEntityStore {
	private final Log log = LogFactory.getLog(HibernateStore.class);
	
	private final SessionFactory sessionFactory;
	
	public HibernateStore() {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure().buildSessionFactory();
		}
		catch(Throwable e) {
			log.fatal("Initial SessionFactory creation failed." + e.getMessage(),e);
            throw new ExceptionInInitializerError(e);
		}
	}
	
	private SessionFactory getSessionFactory() {
        return sessionFactory;
    }
	
	private Session getSession() {
    	Session session = getSessionFactory().getCurrentSession();
    	if(!session.isOpen()) {
    		session = getSessionFactory().openSession();
    	}
    	if(session.getTransaction() == null || !session.getTransaction().isActive()) {
    		session.beginTransaction();
    	}
    	return session;
    }
    
    private void commit() {
    	getSession().getTransaction().commit();
    	getSession().close();
    }

    private void rollback() {
    	getSession().getTransaction().rollback();
    	getSession().close();
    }

    
    private Query prepareQuery(final String query, final Map<String,Object> parameters) {
    	Query q = getSession().createQuery(query);
    	if(parameters != null) {
	    	Iterator<String> i = parameters.keySet().iterator();
	    	while(i.hasNext()) {
	    		String key = i.next();
	    		Object value = parameters.get(key);
	    		q.setParameter(key, value);
	    	}
    	}
    	return q;
    }
    
    private List<?> listObject(final Query q) {
    	List<?> result = q.list();
    	commit();
    	return result;
    }
    
    private Map<String, Object> prepareParameters(Map<String, Object> map, String key, Object value) {
    	if(map == null) {
    		map = new HashMap<String,Object>();
    	}
    	map.put(key, value);
    	return map;
    }
    
	private Object loadObject(final Class<?> objectClass, final Serializable identifier) {
		if(log.isDebugEnabled()) {
			log.debug("Loading " + objectClass + " id: " + identifier);
		}
    	try {
    		Object o = getSession().load(objectClass, identifier);
        	return o;
    	}
    	catch(ObjectNotFoundException e) {
    		return null;
    	}
    }
	
	private Object loadObject(Query q) {
		try {
			Object o = q.uniqueResult();
			commit();
			return o;
		}
		catch(ObjectNotFoundException e) {
			rollback();
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#getColonistGrant(starcorp.common.entities.Colony, starcorp.common.types.PopulationClass, boolean)
	 */
	public ColonistGrant getColonistGrant(Colony colony,
			PopulationClass popClass, boolean openOnly) {
		String q = "from ColonistGrant where colony = :colony and popClassType = :popClass";
		if(openOnly) {
			q = q + " and available = true";
		}
		Map<String, Object> map = prepareParameters(null, "colony", colony);
		prepareParameters(map, "popClass", popClass.getKey());
		return (ColonistGrant) loadObject(prepareQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#getColony(starcorp.common.entities.Planet, starcorp.common.types.Coordinates2D)
	 */
	public Colony getColony(Planet planet, Coordinates2D location) {
		String q = "from Colony where planet = :planet and location = :location";
		Map<String, Object> map = prepareParameters(null, "planet", planet);
		prepareParameters(map, "location", location);
		return (Colony) loadObject(prepareQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#getCorporation(java.lang.String, java.lang.String)
	 */
	public Corporation getCorporation(String email, String password) {
		String q = "from Corporation where playerEmail = :email and playerPassword = :password";
		Map<String, Object> map = prepareParameters(null, "email", email);
		prepareParameters(map, "password", password);
		return (Corporation) loadObject(prepareQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#getDevelopmentGrant(starcorp.common.entities.Colony, starcorp.common.types.AFacilityType, boolean)
	 */
	public DevelopmentGrant getDevelopmentGrant(Colony colony,
			AFacilityType type, boolean openOnly) {
		String q = "from DevelopmentGrant where colony = :colony and type = :type";
		if(openOnly) {
			q = q + " and used = false";
		}
		Map<String, Object> map = prepareParameters(null, "colony", colony);
		prepareParameters(map, "type", type.getKey());
		return (DevelopmentGrant) loadObject(prepareQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#getFacility(starcorp.common.entities.Colony, starcorp.common.entities.Corporation, java.lang.Class)
	 */
	public Facility getFacility(Colony colony, Corporation owner, Class<?> type) {
		List<AFacilityType> types = AFacilityType.listTypes(type);
		if(types.size() > 0) {
			String q = "from Facility where colony = :colony and owner = :owner and type in (";
			for(int i = 0; i < types.size(); i++) {
				if(i > 0)
					q += ", ";
				q += "'" + types.get(i).getKey() + "'";
			}
			q += ")";
			Map<String, Object> map = prepareParameters(null, "colony", colony);
			prepareParameters(map, "owner", owner);
			
			return (Facility) loadObject(prepareQuery(q, map));
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#getFacility(starcorp.common.entities.Colony, java.lang.Class)
	 */
	public Facility getFacility(Colony colony, Class<?> type) {
		List<AFacilityType> types = AFacilityType.listTypes(type);
		if(types.size() > 0) {
			String q = "from Facility where colony = :colony and type in (";
			for(int i = 0; i < types.size(); i++) {
				if(i > 0)
					q += ", ";
				q += "'" + types.get(i).getKey() + "'";
			}
			q += ")";
			Map<String, Object> map = prepareParameters(null, "colony", colony);
			
			return (Facility) loadObject(prepareQuery(q, map));
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#getItem(starcorp.common.entities.Colony, starcorp.common.types.AItemType)
	 */
	public ColonyItem getItem(Colony colony, AItemType type) {
		String q = "from ColonyItem where colony = :colony and item.type = :type";
		Map<String, Object> map = prepareParameters(null, "colony", colony);
		prepareParameters(map, "type", type.getKey());
		return (ColonyItem) loadObject(prepareQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#getItem(starcorp.common.entities.Colony, starcorp.common.entities.Corporation, starcorp.common.types.AItemType)
	 */
	public ColonyItem getItem(Colony colony, Corporation owner, AItemType type) {
		String q = "from ColonyItem where colony = :colony and owner = :owner and item.type = :type";
		Map<String, Object> map = prepareParameters(null, "colony", colony);
		prepareParameters(map, "owner", owner);
		prepareParameters(map, "type", type.getKey());
		return (ColonyItem) loadObject(prepareQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#getLease(starcorp.common.entities.Colony, starcorp.common.entities.Corporation, starcorp.common.types.AFacilityType, boolean)
	 */
	public FacilityLease getLease(Colony colony, Corporation owner,
			AFacilityType type, boolean openOnly) {
		String q = "from FacilityLease where colony = :colony and owner = :owner and type = :type";
		if(openOnly) {
			q = q + " and used = false";
		}
		Map<String, Object> map = prepareParameters(null, "colony", colony);
		prepareParameters(map, "owner", owner);
		prepareParameters(map, "type", type.getKey());
		return (FacilityLease) loadObject(prepareQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#getUnemployed(starcorp.common.entities.Colony, starcorp.common.types.PopulationClass)
	 */
	public Unemployed getUnemployed(Colony colony, PopulationClass popClass) {
		String q = "from Unemployed where colony = :colony and population.popClassType = :popClass";
		Map<String, Object> map = prepareParameters(null, "colony", colony);
		prepareParameters(map, "popClass", popClass.getKey());
		return (Unemployed) loadObject(prepareQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#getWorkers(starcorp.common.entities.Facility, starcorp.common.types.PopulationClass)
	 */
	public Workers getWorkers(Facility facility, PopulationClass popClass) {
		String q = "from Workers where facility = :facility and population.popClassType = :popClass";
		Map<String, Object> map = prepareParameters(null, "facility", facility);
		prepareParameters(map, "popClass", popClass.getKey());
		return (Workers) loadObject(prepareQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listColonies()
	 */
	public List<?> listColonies() {
		String q = "from Colony";
		return listObject(prepareQuery(q, null));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listColonies(starcorp.common.entities.Planet)
	 */
	public List<?> listColonies(Planet planet) {
		String q = "from Colony where planet = :planet";
		Map<String,Object> map = prepareParameters(null, "planet", planet);
		return listObject(prepareQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listColonies(starcorp.common.entities.StarSystem, starcorp.common.types.CoordinatesPolar, starcorp.common.entities.Planet)
	 */
	public List<?> listColonies(StarSystem system,
			CoordinatesPolar location, Planet excludePlanet) {
		String q = "from Colony as col left join col.planet as p where p.ID <> :planetId and p.system = :system and p.location = :location";
		Map<String,Object> map = prepareParameters(null, "planetId", excludePlanet.getID());
		prepareParameters(map, "system", system);
		prepareParameters(map, "location", location);
		return listObject(prepareQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listColonies(starcorp.common.entities.StarSystem, starcorp.common.types.CoordinatesPolar)
	 */
	public List<?> listColonies(StarSystem system,
			CoordinatesPolar excludeLocation) {
		String q = "from Colony as col left join col.planet as p where p.location <> :location and p.system = :system";
		Map<String,Object> map = prepareParameters(null, "location", excludeLocation);
		prepareParameters(map, "system", system);
		return listObject(prepareQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listColonies(starcorp.common.entities.StarSystem)
	 */
	public List<?> listColonies(StarSystem excludeSystem) {
		String q = "from Colony as col left join col.planet as p where p.system <> :system";
		Map<String,Object> map = prepareParameters(null, "system", excludeSystem);
		return listObject(prepareQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listColonistGrants(starcorp.common.entities.Corporation, boolean)
	 */
	public List<?> listColonistGrants(Corporation owner,
			boolean openOnly) {
		String q = "from ColonistGrant grant left join grant.colony as col where col.government = :owner";
		if(openOnly) {
			q = q + " and available = true";
		}
		Map<String, Object> map = prepareParameters(null, "owner", owner);
		return listObject(prepareQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listColonists(starcorp.common.entities.Colony)
	 */
	public List<?> listColonists(Colony colony) {
		String q= "from AColonists where colony = :colony";
		return listObject(prepareQuery(q, prepareParameters(null, "colony", colony)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listColonists(starcorp.common.entities.Colony, starcorp.common.types.PopulationClass)
	 */
	public List<?> listColonists(Colony colony,
			PopulationClass popClass) {
		String q= "from AColonists where colony = :colony and population.popClassType = '" + popClass.getKey() + "'";
		return listObject(prepareQuery(q, prepareParameters(null, "colony", colony)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listDeposits(starcorp.common.entities.AStarSystemEntity)
	 */
	public List<?> listDeposits(AStarSystemEntity systemEntity) {
		String q= "from ResourceDeposit where systemEntity = :systemEntity";
		return listObject(prepareQuery(q, prepareParameters(null, "systemEntity", systemEntity)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listDeposits(starcorp.common.entities.Planet, starcorp.common.types.Coordinates2D)
	 */
	public List<?> listDeposits(Planet planet,
			Coordinates2D location) {
		String q= "from ResourceDeposit where systemEntity = :planet and location = :location";
		Map<String,Object> map = prepareParameters(null, "planet", planet);
		prepareParameters(map, "location", location);
		return listObject(prepareQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listDesigns(starcorp.common.entities.Corporation)
	 */
	public List<?> listDesigns(Corporation owner) {
		String q= "from StarshipDesign where owner = :owner";
		return listObject(prepareQuery(q, prepareParameters(null, "owner", owner)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listDevelopmentGrants(starcorp.common.entities.Corporation, boolean)
	 */
	public List<?> listDevelopmentGrants(Corporation owner,
			boolean openOnly) {
		String q = "from DevelopmentGrant where colony.government = :owner";
		if(openOnly) {
			q = q + " and used = false";
		}
		Map<String, Object> map = prepareParameters(null, "owner", owner);
		return listObject(prepareQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listFacilities()
	 */
	public List<?> listFacilities() {
		String q = "from Facility";
		return listObject(prepareQuery(q, null));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listFacilities(starcorp.common.entities.Colony)
	 */
	public List<?> listFacilities(Colony colony) {
		String q= "from Facility where colony = :colony";
		return listObject(prepareQuery(q, prepareParameters(null, "colony", colony)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listFacilities(starcorp.common.entities.Corporation)
	 */
	public List<?> listFacilities(Corporation owner) {
		String q= "from Facility where owner = :owner";
		return listObject(prepareQuery(q, prepareParameters(null, "owner", owner)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listFacilities(starcorp.common.entities.Colony, java.lang.Class)
	 */
	public List<?> listFacilities(Colony colony, Class<?> type) {
		List<AFacilityType> types = AFacilityType.listTypes(type);
		String q= "from Facility where colony = :colony and type in (";
		for(int i = 0; i < types.size(); i++) {
			if(i > 0)
				q += ", ";
			q += "'" + types.get(i).getKey() + "'";
		}
		q += ")";
		return listObject(prepareQuery(q, prepareParameters(null, "colony", colony)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listFacilitiesBySalary(starcorp.common.entities.Colony, starcorp.common.types.PopulationClass)
	 */
	public List<?> listFacilitiesBySalary(Colony colony,
			PopulationClass popClass) {
		String q= "select facility from Workers where colony = :colony and population.popClassType = '" + popClass.getKey() + "' order by salary desc";
		return listObject(prepareQuery(q, prepareParameters(null, "colony", colony)));
		
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listFacilitiesWithWorkers(starcorp.common.entities.Colony, java.util.List)
	 */
	public Map<Facility, List<?>> listFacilitiesWithWorkers(
			Colony colony, List<AFacilityType> types) {
		String q= "from Facility where colony = :colony and type in (";
		for(int i = 0; i < types.size(); i++) {
			if(i > 0)
				q += ", ";
			q += "'" + types.get(i).getKey() + "'";
		}
		q += ")";
		List<?> facilities = listObject(prepareQuery(q, prepareParameters(null, "colony", colony)));
		Map<Facility, List<?>> result = new HashMap<Facility, List<?>>();
		Iterator<?> i = facilities.iterator();
		while(i.hasNext()) {
			Facility f = (Facility) i.next();
			List<?> workers = listWorkers(f);
			result.put(f, workers);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listItems(starcorp.common.entities.Corporation)
	 */
	public List<?> listItems(Corporation owner) {
		String q= "from ColonyItem where owner = :owner";
		return listObject(prepareQuery(q, prepareParameters(null, "owner", owner)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listItems(starcorp.common.entities.Corporation, starcorp.common.entities.Colony, java.util.List)
	 */
	public List<?> listItems(Corporation owner, Colony colony,
			List<AItemType> types) {
		String q= "from ColonyItem where owner = :owner and item.type in (";
		for(int i = 0; i < types.size(); i++) {
			if(i > 0)
				q += ", ";
			q += "'" + types.get(i).getKey() + "'";
		}
		q += ")";
		return listObject(prepareQuery(q, prepareParameters(null, "owner", owner)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listLeases(starcorp.common.entities.Corporation, boolean)
	 */
	public List<?> listLeases(Corporation corp, boolean openOnly) {
		String q= "from FacilityLease where licensee = :corp";
		if(openOnly) {
			q += " and used = false";
		}
		return listObject(prepareQuery(q, prepareParameters(null, "corp", corp)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listMarket(int)
	 */
	public List<?> listMarket(int minQty) {
		String q= "from MarketItem where item.quantity >= :minQty";
		return listObject(prepareQuery(q, prepareParameters(null, "minQty", minQty)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listMarket(starcorp.common.entities.Colony, int)
	 */
	public List<?> listMarket(Colony colony, int minQty) {
		String q= "from MarketItem where colony = :colony and item.quantity >= " + minQty;
		return listObject(prepareQuery(q, prepareParameters(null, "colony", colony)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listMarket(starcorp.common.entities.Colony, java.util.List, int)
	 */
	public List<?> listMarket(Colony colony, List<AItemType> types,
			int minQty) {
		String q= "from MarketItem where colony = :colony and item.quantity >= " + minQty + " and item.type in (";
		for(int i = 0; i < types.size(); i++) {
			if(i > 0)
				q += ", ";
			q += "'" + types.get(i).getKey() + "'";
		}
		q += ")";
		return listObject(prepareQuery(q, prepareParameters(null, "colony", colony)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listShips(starcorp.common.entities.Corporation)
	 */
	public List<?> listShips(Corporation owner) {
		String q= "from Starship where owner = :owner";
		return listObject(prepareQuery(q, prepareParameters(null, "owner", owner)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listShips(starcorp.common.entities.Planet)
	 */
	public List<?> listShips(Planet orbiting) {
		String q= "from Starship where planet = :orbiting and planetLocation.x < 1 and planetLocation.y < 1";
		return listObject(prepareQuery(q, prepareParameters(null, "orbiting", orbiting)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listShips(starcorp.common.entities.Planet, starcorp.common.types.Coordinates2D)
	 */
	public List<?> listShips(Planet planet, Coordinates2D location) {
		String q= "from Starship where planet = :planet and planetLocation = :location";
		Map<String,Object> map = prepareParameters(null, "planet", planet);
		prepareParameters(map, "location", location);
		return listObject(prepareQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listShips(starcorp.common.entities.Colony)
	 */
	public List<?> listShips(Colony docked) {
		String q= "from Starship where colony = :docked";
		return listObject(prepareQuery(q, prepareParameters(null, "docked", docked)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listSystemEntities(starcorp.common.entities.StarSystem)
	 */
	public List<?> listSystemEntities(StarSystem star) {
		String q= "from AStarSystemEntity where system = :star";
		return listObject(prepareQuery(q, prepareParameters(null, "star", star)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listSystemEntities(starcorp.common.entities.StarSystem, starcorp.common.types.CoordinatesPolar)
	 */
	public List<?> listSystemEntities(StarSystem star,
			CoordinatesPolar location) {
		String q= "from AStarSystemEntity where system = :star and location = :location";
		Map<String,Object> map = prepareParameters(null, "star", star);
		prepareParameters(map, "location", location);
		return listObject(prepareQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listSystems(starcorp.common.types.Coordinates3D, int)
	 */
	public List<?> listSystems(Coordinates3D origin, int range) {
		String q = "from StarSystem where location <> :location";
		List<?> systems = listObject(prepareQuery(q, prepareParameters(null, "location", origin)));
		List<StarSystem> result = new ArrayList<StarSystem>();
		Iterator<?> i = systems.iterator();
		while(i.hasNext()) {
			StarSystem ss = (StarSystem) i.next();
			int distance = ss.getLocation().getDistance(origin);
			if(distance <= range)
				result.add(ss);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listWorkers(starcorp.common.entities.Facility)
	 */
	public List<?> listWorkers(Facility facility) {
		String q= "from Workers where facility = :facility";
		return listObject(prepareQuery(q, prepareParameters(null, "facility", facility)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listWorkers(starcorp.common.entities.Colony)
	 */
	public List<?> listWorkers(Colony colony) {
		String q= "from AColonists where colony = :colony";
		return listObject(prepareQuery(q, prepareParameters(null, "colony", colony)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listWorkers(starcorp.common.entities.Colony, starcorp.common.types.PopulationClass)
	 */
	public List<?> listWorkers(Colony colony, PopulationClass popClass) {
		String q= "from AColonists where colony = :colony and population.popClassType = '" + popClass.getKey() + "'";
		return listObject(prepareQuery(q, prepareParameters(null, "colony", colony)));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#save(starcorp.common.entities.ABaseEntity)
	 */
	public ABaseEntity save(ABaseEntity entity) {
		getSession().saveOrUpdate(entity);
		commit();
		return entity;
	}

	public ABaseEntity load(Class<?> entityClass, int ID) {
		return (ABaseEntity) loadObject(entityClass, ID);
	}

	public Corporation getCorporation(String email) {
		String q = "from Corporation where playerEmail = :email";
		Map<String, Object> map = prepareParameters(null, "email", email);
		return (Corporation) loadObject(prepareQuery(q, map));
	}

	public List<?> list(Class<?> entityClass) {
		String q = "from " + entityClass.getSimpleName();
		return listObject(prepareQuery(q, null));
	}

	public void delete(Class<?> entityClass, int ID) {
		getSession().delete(loadObject(entityClass, ID));
		commit();
	}

	public List<?> query(String hql) {
		return listObject(prepareQuery(hql, null));
	}

}
