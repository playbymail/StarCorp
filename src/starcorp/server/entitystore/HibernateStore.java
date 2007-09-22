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
import starcorp.common.entities.CreditAccount;
import starcorp.common.entities.StarSystemEntity;
import starcorp.common.entities.ColonistGrant;
import starcorp.common.entities.Colony;
import starcorp.common.entities.ColonyItem;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.DevelopmentGrant;
import starcorp.common.entities.Facility;
import starcorp.common.entities.FacilityLease;
import starcorp.common.entities.Planet;
import starcorp.common.entities.StarSystem;
import starcorp.common.entities.Starship;
import starcorp.common.entities.Unemployed;
import starcorp.common.entities.Workers;
import starcorp.common.types.AFacilityType;
import starcorp.common.types.AItemType;
import starcorp.common.types.AtmosphereType;
import starcorp.common.types.Coordinates2D;
import starcorp.common.types.Coordinates3D;
import starcorp.common.types.CoordinatesPolar;
import starcorp.common.types.GalacticDate;
import starcorp.common.types.PopulationClass;
import starcorp.server.ServerConfiguration;

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
    	return getSessionFactory().getCurrentSession();
    }
	
	public void beginTransaction() {
		getSession().beginTransaction();
	}
    
    public void commit() {
    	getSession().flush();
    	getSession().getTransaction().commit();
    }
    
    public void rollback() {
		getSession().getTransaction().rollback();
	}


    private Query createQuery(final String query, final Map<String,Object> parameters) {
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
    
    private Query createQuery(final String query, final String paramName, final Object paramValue) {
    	Query q = getSession().createQuery(query);
		q.setParameter(paramName, paramValue);
    	return q;
    }

    private Query createQuery(final String query) {
    	Query q = getSession().createQuery(query);
    	return q;
    }

    private List<?> listObject(final Query q) {
    	List<?> result = q.list();
    	return result;
    }
    
    private Map<String, Object> prepareParameters(String key, Object value) {
    	return prepareParameters(null, key, value);
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
			return o;
		}
		catch(ObjectNotFoundException e) {
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
		Map<String, Object> map = prepareParameters("colony", colony);
		prepareParameters(map, "popClass", popClass.getKey());
		return (ColonistGrant) loadObject(createQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#getColony(starcorp.common.entities.Planet, starcorp.common.types.Coordinates2D)
	 */
	public Colony getColony(Planet planet, Coordinates2D location) {
		String q = "from Colony where planet = :planet and location = :location";
		Map<String, Object> map = prepareParameters("planet", planet);
		prepareParameters(map, "location", location);
		return (Colony) loadObject(createQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#getCorporation(java.lang.String, java.lang.String)
	 */
	public Corporation getCorporation(String email, String password) {
		String q = "from Corporation where playerEmail = :email and playerPassword = :password";
		Map<String, Object> map = prepareParameters("email", email);
		prepareParameters(map, "password", password);
		return (Corporation) loadObject(createQuery(q, map));
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
		Map<String, Object> map = prepareParameters("colony", colony);
		prepareParameters(map, "type", type.getKey());
		return (DevelopmentGrant) loadObject(createQuery(q, map));
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
			Map<String, Object> map = prepareParameters("colony", colony);
			prepareParameters(map, "owner", owner);
			
			return (Facility) loadObject(createQuery(q, map));
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
			return (Facility) loadObject(createQuery(q, "colony",colony));
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#getItem(starcorp.common.entities.Colony, starcorp.common.types.AItemType)
	 */
	public ColonyItem getItem(Colony colony, AItemType type) {
		String q = "from ColonyItem where colony = :colony and item.type = :type";
		Map<String, Object> map = prepareParameters("colony", colony);
		prepareParameters(map, "type", type.getKey());
		return (ColonyItem) loadObject(createQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#getItem(starcorp.common.entities.Colony, starcorp.common.entities.Corporation, starcorp.common.types.AItemType)
	 */
	public ColonyItem getItem(Colony colony, Corporation owner, AItemType type) {
		String q = "from ColonyItem where colony = :colony and owner = :owner and item.type = :type";
		Map<String, Object> map = prepareParameters("colony", colony);
		prepareParameters(map, "owner", owner);
		prepareParameters(map, "type", type.getKey());
		return (ColonyItem) loadObject(createQuery(q, map));
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
		Map<String, Object> map = prepareParameters("colony", colony);
		prepareParameters(map, "owner", owner);
		prepareParameters(map, "type", type.getKey());
		return (FacilityLease) loadObject(createQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#getUnemployed(starcorp.common.entities.Colony, starcorp.common.types.PopulationClass)
	 */
	public Unemployed getUnemployed(Colony colony, PopulationClass popClass) {
		String q = "from Unemployed where colony = :colony and population.popClassType = :popClass";
		Map<String, Object> map = prepareParameters("colony", colony);
		prepareParameters(map, "popClass", popClass.getKey());
		return (Unemployed) loadObject(createQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#getWorkers(starcorp.common.entities.Facility, starcorp.common.types.PopulationClass)
	 */
	public Workers getWorkers(Facility facility, PopulationClass popClass) {
		String q = "from Workers where facility = :facility and population.popClassType = :popClass";
		Map<String, Object> map = prepareParameters("facility", facility);
		prepareParameters(map, "popClass", popClass.getKey());
		return (Workers) loadObject(createQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listColonies()
	 */
	public List<?> listColonies() {
		String q = "from Colony";
		return listObject(createQuery(q));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listColonies(starcorp.common.entities.Planet)
	 */
	public List<?> listColonies(Planet planet) {
		String q = "from Colony where planet = :planet";
		return listObject(createQuery(q, "planet", planet));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listColonies(starcorp.common.entities.StarSystem, starcorp.common.types.CoordinatesPolar, starcorp.common.entities.Planet)
	 */
	public List<?> listColonies(StarSystem system,
			CoordinatesPolar location, Planet excludePlanet) {
		String q = "from Colony as col left join col.planet as p where p.ID <> :planetId and p.system = :system and p.location = :location";
		Map<String,Object> map = prepareParameters("planetId", excludePlanet.getID());
		prepareParameters(map, "system", system);
		prepareParameters(map, "location", location);
		return listObject(createQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listColonies(starcorp.common.entities.StarSystem, starcorp.common.types.CoordinatesPolar)
	 */
	public List<?> listColonies(StarSystem system,
			CoordinatesPolar excludeLocation) {
		String q = "from Colony as col left join col.planet as p where p.location <> :location and p.system = :system";
		Map<String,Object> map = prepareParameters("location", excludeLocation);
		prepareParameters(map, "system", system);
		return listObject(createQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listColonies(starcorp.common.entities.StarSystem)
	 */
	public List<?> listColonies(StarSystem excludeSystem) {
		String q = "from Colony as col left join col.planet as p where p.system <> :system";
		return listObject(createQuery(q, "system", excludeSystem));
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
		Map<String, Object> map = prepareParameters("owner", owner);
		return listObject(createQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listColonists(starcorp.common.entities.Colony)
	 */
	public List<?> listColonists(Colony colony) {
		String q= "from AColonists where colony = :colony";
		return listObject(createQuery(q, "colony", colony));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listColonists(starcorp.common.entities.Colony, starcorp.common.types.PopulationClass)
	 */
	public List<?> listColonists(Colony colony,
			PopulationClass popClass) {
		String q= "from AColonists where colony = :colony and population.popClassType = '" + popClass.getKey() + "'";
		return listObject(createQuery(q, "colony", colony));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listDeposits(starcorp.common.entities.AStarSystemEntity)
	 */
	public List<?> listDeposits(StarSystemEntity systemEntity) {
		String q= "from ResourceDeposit where systemEntity = :systemEntity";
		return listObject(createQuery(q, "systemEntity", systemEntity));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listDeposits(starcorp.common.entities.Planet, starcorp.common.types.Coordinates2D)
	 */
	public List<?> listDeposits(Planet planet,
			Coordinates2D location) {
		String q= "from ResourceDeposit where systemEntity = :planet and location = :location";
		Map<String,Object> map = prepareParameters("planet", planet);
		prepareParameters(map, "location", location);
		return listObject(createQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listDesigns(starcorp.common.entities.Corporation)
	 */
	public List<?> listDesigns(Corporation owner) {
		String q= "from StarshipDesign where owner = :owner";
		return listObject(createQuery(q, "owner", owner));
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
		return listObject(createQuery(q, "owner", owner));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listFacilities()
	 */
	public List<?> listFacilities() {
		String q = "from Facility";
		return listObject(createQuery(q));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listFacilities(starcorp.common.entities.Colony)
	 */
	public List<?> listFacilities(Colony colony) {
		String q= "from Facility where colony = :colony";
		return listObject(createQuery(q, "colony", colony));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listFacilities(starcorp.common.entities.Corporation)
	 */
	public List<?> listFacilities(Corporation owner) {
		String q= "from Facility where owner = :owner";
		return listObject(createQuery(q, "owner", owner));
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
		return listObject(createQuery(q, "colony", colony));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listFacilitiesBySalary(starcorp.common.entities.Colony, starcorp.common.types.PopulationClass)
	 */
	public List<?> listFacilitiesBySalary(PopulationClass popClass) {
		String q= "select facility from Workers as w where w.population.popClassType = '" + popClass.getKey() + "' order by salary desc";
		return listObject(createQuery(q));
		
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
		List<?> facilities = listObject(createQuery(q, "colony", colony));
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
		return listObject(createQuery(q, "owner", owner));
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
		return listObject(createQuery(q, "owner", owner));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listLeases(starcorp.common.entities.Corporation, boolean)
	 */
	public List<?> listLeases(Corporation corp, boolean openOnly) {
		String q= "from FacilityLease where licensee = :corp";
		if(openOnly) {
			q += " and used = false";
		}
		return listObject(createQuery(q, "corp", corp));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listMarket(int)
	 */
	public List<?> listMarket(int minQty) {
		String q= "from MarketItem where item.quantity >= :minQty";
		return listObject(createQuery(q, "minQty", minQty));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listMarket(starcorp.common.entities.Colony, int)
	 */
	public List<?> listMarket(Colony colony, int minQty) {
		String q= "from MarketItem where colony = :colony and item.quantity >= " + minQty;
		return listObject(createQuery(q, "colony", colony));
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
		return listObject(createQuery(q, "colony", colony));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listShips(starcorp.common.entities.Corporation)
	 */
	public List<?> listShips(Corporation owner) {
		String q= "from Starship where owner = :owner";
		return listObject(createQuery(q, "owner", owner));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listShips(starcorp.common.entities.Planet)
	 */
	public List<?> listShips(Planet orbiting) {
		String q= "from Starship where planet = :orbiting and planetLocation.x < 1 and planetLocation.y < 1";
		return listObject(createQuery(q, "orbiting", orbiting));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listShips(starcorp.common.entities.Planet, starcorp.common.types.Coordinates2D)
	 */
	public List<?> listShips(Planet planet, Coordinates2D location) {
		String q= "from Starship where planet = :planet and planetLocation = :location";
		Map<String,Object> map = prepareParameters("planet", planet);
		prepareParameters(map, "location", location);
		return listObject(createQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listShips(starcorp.common.entities.Colony)
	 */
	public List<?> listShips(Colony docked) {
		String q= "from Starship where colony = :docked";
		return listObject(createQuery(q, "docked", docked));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listSystemEntities(starcorp.common.entities.StarSystem)
	 */
	public List<?> listSystemEntities(StarSystem star) {
		String q= "from AStarSystemEntity where system = :star";
		return listObject(createQuery(q, "star", star));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listSystemEntities(starcorp.common.entities.StarSystem, starcorp.common.types.CoordinatesPolar)
	 */
	public List<?> listSystemEntities(StarSystem star,
			CoordinatesPolar location) {
		String q= "from AStarSystemEntity where system = :star and location = :location";
		Map<String,Object> map = prepareParameters("star", star);
		prepareParameters(map, "location", location);
		return listObject(createQuery(q, map));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listSystems(starcorp.common.types.Coordinates3D, int)
	 */
	public List<?> listSystems(Coordinates3D origin, int range) {
		String q = "from StarSystem where location <> :location";
		List<?> systems = listObject(createQuery(q, "location", origin));
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
		return listObject(createQuery(q, "facility", facility));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listWorkers(starcorp.common.entities.Colony)
	 */
	public List<?> listWorkers(Colony colony) {
		String q= "from AColonists where colony = :colony";
		return listObject(createQuery(q, "colony", colony));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#listWorkers(starcorp.common.entities.Colony, starcorp.common.types.PopulationClass)
	 */
	public List<?> listWorkers(Colony colony, PopulationClass popClass) {
		String q= "from AColonists where colony = :colony and population.popClassType = '" + popClass.getKey() + "'";
		return listObject(createQuery(q, "colony", colony));
	}

	/* (non-Javadoc)
	 * @see starcorp.server.entitystore.IEntityStore#save(starcorp.common.entities.ABaseEntity)
	 */
	public ABaseEntity save(ABaseEntity entity) {
		getSession().saveOrUpdate(entity);
		return entity;
	}

	public ABaseEntity load(Class<?> entityClass, long ID) {
		return (ABaseEntity) loadObject(entityClass, ID);
	}

	public Corporation getCorporation(String email) {
		String q = "from Corporation where playerEmail = :email";
		return (Corporation) loadObject(createQuery(q, "email", email));
	}

	public List<?> list(Class<?> entityClass) {
		String q = "from " + entityClass.getSimpleName();
		return listObject(createQuery(q));
	}

	public List<?> query(String hql) {
		return listObject(createQuery(hql));
	}

	public List<?> listPlanets(StarSystem star, int maxGravity,
			List<AtmosphereType> atmospheres) {
		String q = "from Planet where system = :system and gravityRating <= :maxGravity";
		int max = atmospheres == null ? 0 : atmospheres.size();
		if(max > 0) {
			q += " and atmosphereType in (";
		}
		for(int i = 0; i < max; i++) {
			if(i > 0)
				q += ", ";
			q += "'" + atmospheres.get(i).getKey() + "'";
		}
		if(max > 0) {
			q += ")";
		}
		Map<String,Object> map = prepareParameters("system", star);
		map = prepareParameters(map, "maxGravity", maxGravity);
		return listObject(createQuery(q, map));
	}

	public void shutdown() {
		sessionFactory.close();
	}

	public List<?> listDeposits(Planet planet, List<AItemType> types,
			int minTotal) {
		String q = "from ResourceDeposit where systemEntity = :planet and totalQuantity >= " + minTotal;
		int max = types == null ? 0 : types.size();
		if(max > 0) {
			q += " and type IN (";
		}
		for(int i = 0; i < max; i++) {
			if(i > 0)
				q += ", ";
			q += "'" + types.get(i).getKey() + "'";
		}
		if(max > 0) {
			q += ")";
		}
		q += " order by yield desc";
		return listObject(createQuery(q, "planet", planet));
	}
	
	public void delete(ABaseEntity entity) {
		if(entity instanceof Colony) {
			deleteColony((Colony)entity);
		}
		else if(entity instanceof Corporation) {
			deleteCorp((Corporation)entity);
		}
		else if(entity instanceof Facility) {
			deleteFacility((Facility)entity);
		}
		else if(entity instanceof Planet) {
			deletePlanet((Planet)entity);
		}
		else if(entity instanceof StarSystem) {
			deleteSystem((StarSystem)entity);
		}
		else if(entity instanceof StarSystemEntity) {
			deleteSystemEntity((StarSystemEntity)entity);
		}
		else {
			getSession().delete(entity);
		}
	}

	private void deleteColony(Colony colony) {
		String q = "delete from AColonists where colony = :colony";
		Query query = createQuery(q,"colony",colony);
		query.executeUpdate();
		q = "delete from ColonistGrant where colony = :colony";
		query = createQuery(q,"colony",colony);
		query.executeUpdate();
		q = "delete from ColonyItem where colony = :colony";
		query = createQuery(q,"colony",colony);
		query.executeUpdate();
		q = "delete from DevelopmentGrant where colony = :colony";
		query = createQuery(q,"colony",colony);
		query.executeUpdate();
		q = "delete from ColonyItem where colony = :colony";
		query = createQuery(q,"colony",colony);
		query.executeUpdate();
		List<?> facilities = listFacilities(colony);
		for(Object o : facilities) {
			deleteFacility((Facility)o);
		}
		q = "delete from FacilityLease where colony = :colony";
		query = createQuery(q,"colony",colony);
		query.executeUpdate();
		q = "delete from MarketItem where colony = :colony";
		query = createQuery(q,"colony",colony);
		query.executeUpdate();
		getSession().delete(colony);
	}

	private void deleteCorp(Corporation corp) {
		Session session = getSession();
		List<?> colonies = listColonies(corp);
		for(Object o : colonies) {
			deleteColony((Colony)o);
		}
		List<?> ships = listShips(corp);
		for(Object o : ships) {
			deleteSystemEntity((Starship)o);
		}
		List<?> facilities = listFacilities(corp);
		for(Object o : facilities) {
			deleteFacility((Facility)o);
		}
		String q = "delete from MarketItem where seller = :corp";
		Query query = createQuery(q,"corp",corp);
		query.executeUpdate();
		q = "delete from ColonyItem where owner = :corp";
		query = createQuery(q,"corp",corp);
		query.executeUpdate();
		q = "delete from StarshipDesign where owner = :corp";
		query = createQuery(q,"corp",corp);
		query.executeUpdate();
		session.delete(corp);
	}

	private void deleteFacility(Facility facility) {
		Session session = getSession();
		String q = "delete from Workers where facility = :facility";
		Query query = createQuery(q, "facility", facility);
		query.executeUpdate();
		session.delete(facility);
	}

	private void deletePlanet(Planet planet) {
		List<?> colonies = listColonies(planet);
		for(Object o : colonies) {
			Colony c = (Colony) o;
			deleteColony(c);
		}
		Session session = getSession();
		String q = "delete ResourceDeposit where systemEntity = :entity";
		Query query = createQuery(q, "entity", planet);
		query.executeUpdate();
		session.delete(planet);
	}

	private void deleteSystem(StarSystem system) {
		Session session = getSession();
		List<?> entities = listSystemEntities(system);
		for(Object o : entities) {
			deleteSystemEntity((StarSystemEntity) o);
		}
		session.delete(system);
	}

	private void deleteSystemEntity(StarSystemEntity entity) {
		if(entity instanceof Planet) {
			deletePlanet((Planet)entity);
		}
		else {
			Session session = getSession();
			String q = "delete ResourceDeposit where systemEntity = :entity";
			Query query = createQuery(q, "entity", entity);
			query.executeUpdate();
			session.delete(entity);
		}
	}

	public List<?> listColonies(Corporation govt) {
		String q = "from Colony where government = :govt";
		return listObject(createQuery(q, "govt", govt));
	}

	public List<?> listColonists() {
		String q = "from AColonists";
		return listObject(createQuery(q));
	}

	public List<?> listColonistGrants(Colony colony, boolean openOnly) {
		String q = "from ColonistGrant where colony = :colony";
		if(openOnly) {
			q += " and available = true";
		}
		return listObject(createQuery(q, "colony", colony));
	}

	public List<?> listUnemployed() {
		String q = "from Unemployed where population.quantity > 0";
		return listObject(createQuery(q));
	}
	
	// TODO fix locking issue!
	
	public int addCredits(ABaseEntity entity, int credits, String reason) {
		beginTransaction();
		CreditAccount acct = getAccount(entity);
		acct.add(credits, ServerConfiguration.getCurrentDate(), reason);
		getSession().save(acct);
		int balance = acct.getCredits();
		commit();
		return balance;
	}
	
	public int getCredits(ABaseEntity entity) {
		CreditAccount acct = getAccount(entity);
		int credits = acct.getCredits();
		return credits;
	}

	public int transferCredits(ABaseEntity from, ABaseEntity to, int credits,
			String reason) {
		beginTransaction();
		GalacticDate date = ServerConfiguration.getCurrentDate();
		CreditAccount acctFrom = getAccount(from);
		CreditAccount acctTo = getAccount(to);
		acctFrom.remove(credits, date, reason);
		acctTo.add(credits, date, reason);
		int balance = acctTo.getCredits();
		commit();
		return balance;
	}

	private CreditAccount getAccount(ABaseEntity entity) {
		String q = "select account from CreditAccount as account where accountHolder = :entity";
		Query query = createQuery(q, "entity", entity);
		CreditAccount acct = (CreditAccount) query.uniqueResult();
		if(acct == null) {
			acct = new CreditAccount();
			acct.setAccountHolder(entity);
			getSession().save(acct);
		}
		return acct;
	}
	
	public int removeCredits(ABaseEntity entity, int credits, String reason) {
		beginTransaction();
		CreditAccount acct = getAccount(entity);
		acct.remove(credits, ServerConfiguration.getCurrentDate(), reason);
		getSession().save(acct);
		int balance = acct.getCredits();
		commit();
		return balance;
	}

	public List<?> listAccounts() {
		String q = "from CreditAccount";
		return listObject(createQuery(q));
	}

}
