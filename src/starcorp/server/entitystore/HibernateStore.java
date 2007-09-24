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

import starcorp.common.entities.AColonists;
import starcorp.common.entities.ACorporateItem;
import starcorp.common.entities.AGovernmentLaw;
import starcorp.common.entities.CashTransaction;
import starcorp.common.entities.CreditAccount;
import starcorp.common.entities.FactoryQueueItem;
import starcorp.common.entities.IEntity;
import starcorp.common.entities.MarketItem;
import starcorp.common.entities.ResourceDeposit;
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
import starcorp.common.entities.StarshipDesign;
import starcorp.common.entities.Unemployed;
import starcorp.common.entities.Workers;
import starcorp.common.types.AFacilityType;
import starcorp.common.types.AItemType;
import starcorp.common.types.AtmosphereType;
import starcorp.common.types.Coordinates2D;
import starcorp.common.types.Coordinates3D;
import starcorp.common.types.CoordinatesPolar;
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
			sessionFactory = new Configuration().configure()
					.buildSessionFactory();
		} catch (Throwable e) {
			log.fatal("Initial SessionFactory creation failed."
					+ e.getMessage(), e);
			throw new ExceptionInInitializerError(e);
		}
	}
	
	private void beginTransaction() {
		getSession().beginTransaction();
	}

	private void commit() {
		getSession().flush();
		getSession().getTransaction().commit();
		getSession().close();
	}

	private List<CreditAccount> copyAccounts(List<?> objects) {
		List<CreditAccount> list = new ArrayList<CreditAccount>();
		for (Object o : objects) {
			list.add((CreditAccount) o);
		}
		commit();
		return list;
	}

	private List<Colony> copyColonies(List<?> objects) {
		List<Colony> list = new ArrayList<Colony>();
		for (Object o : objects) {
			list.add((Colony) o);
		}
		commit();
		return list;
	}

	private List<AColonists> copyColonists(List<?> objects) {
		List<AColonists> list = new ArrayList<AColonists>();
		for (Object o : objects) {
			list.add((AColonists) o);
		}
		commit();
		return list;
	}

	private List<ResourceDeposit> copyDeposits(List<?> objects) {
		List<ResourceDeposit> list = new ArrayList<ResourceDeposit>();
		for (Object o : objects) {
			list.add((ResourceDeposit) o);
		}
		commit();
		return list;
	}

	private List<StarshipDesign> copyDesigns(List<?> objects) {
		List<StarshipDesign> list = new ArrayList<StarshipDesign>();
		for (Object o : objects) {
			list.add((StarshipDesign) o);
		}
		commit();
		return list;
	}

	private List<DevelopmentGrant> copyDevGrants(List<?> objects) {
		List<DevelopmentGrant> list = new ArrayList<DevelopmentGrant>();
		for (Object o : objects) {
			list.add((DevelopmentGrant) o);
		}
		commit();
		return list;
	}

	private List<Facility> copyFacilities(List<?> objects) {
		List<Facility> list = new ArrayList<Facility>();
		for (Object o : objects) {
			list.add((Facility) o);
		}
		commit();
		return list;
	}

	private List<ColonistGrant> copyGrants(List<?> objects) {
		List<ColonistGrant> list = new ArrayList<ColonistGrant>();
		for (Object o : objects) {
			list.add((ColonistGrant) o);
		}
		commit();
		return list;
	}

	private List<ColonyItem> copyItems(List<?> objects) {
		List<ColonyItem> list = new ArrayList<ColonyItem>();
		for (Object o : objects) {
			list.add((ColonyItem) o);
		}
		commit();
		return list;
	}

	private List<FacilityLease> copyLeases(List<?> objects) {
		List<FacilityLease> list = new ArrayList<FacilityLease>();
		for (Object o : objects) {
			list.add((FacilityLease) o);
		}
		commit();
		return list;
	}

	private List<MarketItem> copyMarket(List<?> objects) {
		List<MarketItem> list = new ArrayList<MarketItem>();
		for (Object o : objects) {
			list.add((MarketItem) o);
		}
		commit();
		return list;
	}

	private List<Object> copyObjects(List<?> objects) {
		List<Object> list = new ArrayList<Object>();
		for (Object o : objects) {
			list.add(o);
		}
		commit();
		return list;
	}

	private List<Planet> copyPlanets(List<?> objects) {
		List<Planet> list = new ArrayList<Planet>();
		for (Object o : objects) {
			list.add((Planet) o);
		}
		commit();
		return list;
	}

	private List<Starship> copyShips(List<?> objects) {
		List<Starship> list = new ArrayList<Starship>();
		for (Object o : objects) {
			list.add((Starship) o);
		}
		commit();
		return list;
	}

	private List<StarSystemEntity> copySystemEntities(List<?> objects) {
		List<StarSystemEntity> list = new ArrayList<StarSystemEntity>();
		for (Object o : objects) {
			list.add((StarSystemEntity) o);
		}
		commit();
		return list;
	}

	private Query createQuery(final String query) {
		Query q = getSession().createQuery(query);
		return q;
	}

	private Query createQuery(final String query,
			final Map<String, Object> parameters) {
		Query q = getSession().createQuery(query);
		if (parameters != null) {
			Iterator<String> i = parameters.keySet().iterator();
			while (i.hasNext()) {
				String key = i.next();
				Object value = parameters.get(key);
				q.setParameter(key, value);
			}
		}
		return q;
	}

	private Query createQuery(final String query, final String paramName,
			final Object paramValue) {
		Query q = getSession().createQuery(query);
		q.setParameter(paramName, paramValue);
		return q;
	}

	private void deleteColony(Colony colony) {
		beginTransaction();
		String q = "delete from AColonists where colony = :colony";
		Query query = createQuery(q, "colony", colony);
		query.executeUpdate();
		q = "delete from ColonistGrant where colony = :colony";
		query = createQuery(q, "colony", colony);
		query.executeUpdate();
		q = "delete from ColonyItem where colony = :colony";
		query = createQuery(q, "colony", colony);
		query.executeUpdate();
		q = "delete from DevelopmentGrant where colony = :colony";
		query = createQuery(q, "colony", colony);
		query.executeUpdate();
		q = "delete from ColonyItem where colony = :colony";
		query = createQuery(q, "colony", colony);
		query.executeUpdate();
		q = "delete from FacilityLease where colony = :colony";
		query = createQuery(q, "colony", colony);
		query.executeUpdate();
		q = "delete from MarketItem where colony = :colony";
		query = createQuery(q, "colony", colony);
		query.executeUpdate();
		commit();
		List<Facility> facilities = listFacilities(colony);
		for (Object o : facilities) {
			deleteFacility((Facility) o);
		}
		beginTransaction();
		getSession().delete(colony);
		commit();
	}

	private void deleteCorp(Corporation corp) {
		List<Colony> colonies = listColonies(corp);
		for (Object o : colonies) {
			deleteColony((Colony) o);
		}
		List<Starship> ships = listShips(corp);
		for (Object o : ships) {
			deleteSystemEntity((Starship) o);
		}
		List<Facility> facilities = listFacilities(corp);
		for (Object o : facilities) {
			deleteFacility((Facility) o);
		}
		beginTransaction();
		String q = "delete from MarketItem where owner = :corp";
		Query query = createQuery(q, "corp", corp);
		query.executeUpdate();
		q = "delete from ColonyItem where owner = :corp";
		query = createQuery(q, "corp", corp);
		query.executeUpdate();
		q = "delete from StarshipDesign where owner = :corp";
		query = createQuery(q, "corp", corp);
		query.executeUpdate();
		getSession().delete(corp);
		commit();
	}

	private void deleteFacility(Facility facility) {
		beginTransaction();
		String q = "delete from Workers where facility = :facility";
		Query query = createQuery(q, "facility", facility);
		query.executeUpdate();
		getSession().delete(facility);
		commit();
	}

	private void deletePlanet(Planet planet) {
		List<Colony> colonies = listColonies(planet);
		for (Object o : colonies) {
			Colony c = (Colony) o;
			deleteColony(c);
		}
		beginTransaction();
		String q = "delete ResourceDeposit where systemEntityID = " + planet.getID();
		Query query = createQuery(q);
		query.executeUpdate();
		getSession().delete(planet);
		commit();
	}

	private void deleteSystem(StarSystem system) {
		List<StarSystemEntity> entities = listSystemEntities(system);
		for (Object o : entities) {
			deleteSystemEntity((StarSystemEntity) o);
		}
		beginTransaction();
		getSession().delete(system);
		commit();
	}

	private void deleteSystemEntity(StarSystemEntity entity) {
		if (entity instanceof Planet) {
			deletePlanet((Planet) entity);
		} else {
			beginTransaction();
			String q = "delete ResourceDeposit where systemEntityID = " + entity.getID();
			Query query = createQuery(q);
			query.executeUpdate();
			getSession().delete(entity);
			commit();
		}
	}

	private Session getSession() {
		return getSessionFactory().getCurrentSession();
	}

	private SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	private List<?> listObject(final Query q) {
		List<?> result = q.list();
		return result;
	}

	private Object loadObject(final Class<?> objectClass,
			final Serializable identifier) {
		if (log.isDebugEnabled()) {
			log.debug("Loading " + objectClass + " id: " + identifier);
		}
		try {
			Object o = getSession().load(objectClass, identifier);
			return o;
		} catch (ObjectNotFoundException e) {
			return null;
		}
	}

	private Object loadObject(Query q) {
		try {
			Object o = q.uniqueResult();
			return o;
		} catch (ObjectNotFoundException e) {
			return null;
		}
	}

	private Map<String, Object> prepareParameters(Map<String, Object> map,
			String key, Object value) {
		if (map == null) {
			map = new HashMap<String, Object>();
		}
		map.put(key, value);
		return map;
	}

	private Map<String, Object> prepareParameters(String key, Object value) {
		return prepareParameters(null, key, value);
	}

	private Query queryColonistGrants(Colony colony, boolean openOnly) {
		String q = "from ColonistGrant where colony = :colony";
		if (openOnly) {
			q += " and available = true";
		}
		return createQuery(q, "colony", colony);
	}

	private Query queryFacilitiesByType (
			Colony colony, List<AFacilityType> types){
		String q = "select w from Workers as w left join w.facility as facility where w.colony = :colony and facility.type in (";
		for (int i = 0; i < types.size(); i++) {
			if (i > 0)
				q += ", ";
			q += "'" + types.get(i).getKey() + "'";
		}
		q += ")";
		return createQuery(q,"colony",colony);
	}

	private Query queryMarket(Colony colony, List<AItemType> types,
			int minQty) {
		String q = "from MarketItem where colony = :colony and item.quantity >= "
					+ minQty + " and item.type in (";
			for (int i = 0; i < types.size(); i++) {
				if (i > 0)
					q += ", ";
				q += "'" + types.get(i).getKey() + "'";
			}
			q += ")";
			return createQuery(q, "colony", colony);
	}

	public long addCredits(IEntity entity, long credits, String reason) {
		String q = "update versioned CreditAccount set credits = credits + " + 
		credits + " where ID = " + entity.getID();
		beginTransaction();
		Query query = createQuery(q);
		int updated = query.executeUpdate();
		if(updated == 0) {
			CreditAccount acct = new CreditAccount();
			acct.setID(entity.getID());
			acct.setCredits(credits);
			getSession().save(acct);
		}
		CashTransaction transaction = new CashTransaction();
		transaction.setAccountID(entity.getID());
		transaction.setAmount(credits);
		transaction.setDate(ServerConfiguration.getCurrentDate());
		transaction.setDescription(reason);
		getSession().save(transaction);
		commit();
		return getCredits(entity);
	}

	public IEntity create(IEntity entity) {
		beginTransaction();
		getSession().saveOrUpdate(entity);
		commit();
		return entity;
	}

	public AGovernmentLaw create(AGovernmentLaw law) {
		beginTransaction();
		getSession().saveOrUpdate(law);
		commit();
		return law;
	}

	public ACorporateItem create(ACorporateItem item) {
		beginTransaction();
		getSession().saveOrUpdate(item);
		commit();
		return item;
	}

	public ResourceDeposit create(ResourceDeposit deposit) {
		beginTransaction();
		getSession().saveOrUpdate(deposit);
		commit();
		return deposit;
	}

	public void delete(ACorporateItem item) {
		beginTransaction();
		getSession().delete(item);
		commit();
	}
	public void delete(IEntity entity) {
		if (entity instanceof Colony) {
			deleteColony((Colony) entity);
		} else if (entity instanceof Corporation) {
			deleteCorp((Corporation) entity);
		} else if (entity instanceof Facility) {
			deleteFacility((Facility) entity);
		} else if (entity instanceof Planet) {
			deletePlanet((Planet) entity);
		} else if (entity instanceof StarSystem) {
			deleteSystem((StarSystem) entity);
		} else if (entity instanceof StarSystemEntity) {
			deleteSystemEntity((StarSystemEntity) entity);
		} else {
			beginTransaction();
			getSession().delete(entity);
			commit();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#getColonistGrant(starcorp.common.entities.Colony,
	 *      starcorp.common.types.PopulationClass, boolean)
	 */
	public AGovernmentLaw getColonistGrant(Colony colony,
			PopulationClass popClass, boolean openOnly) {
		String q = "from ColonistGrant where colony = :colony and popClassType = :popClass";
		if (openOnly) {
			q = q + " and available = true";
		}
		Map<String, Object> map = prepareParameters("colony", colony);
		prepareParameters(map, "popClass", popClass.getKey());
		beginTransaction();
		AGovernmentLaw grant = (AGovernmentLaw) loadObject(createQuery(q, map));
		commit();
		return grant;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#getColony(starcorp.common.entities.Planet,
	 *      starcorp.common.types.Coordinates2D)
	 */
	public Colony getColony(Planet planet, Coordinates2D location) {
		String q = "from Colony where planetID = " + planet.getID() + " and location = :location";
		beginTransaction();
		Colony colony = (Colony) loadObject(createQuery(q, "location", location));
		commit();
		return colony;
	}

	public Corporation getCorporation(String email) {
		String q = "from Corporation where playerEmail = :email";
		beginTransaction();
		Corporation c = (Corporation) loadObject(createQuery(q, "email", email));
		commit();
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#getCorporation(java.lang.String,
	 *      java.lang.String)
	 */
	public Corporation getCorporation(String email, String password) {
		String q = "from Corporation where playerEmail = :email and playerPassword = :password";
		Map<String, Object> map = prepareParameters("email", email);
		prepareParameters(map, "password", password);
		beginTransaction();
		Corporation c = (Corporation) loadObject(createQuery(q, map));
		commit();
		return c;
	}

	public long getCredits(IEntity entity) {
		beginTransaction();
		String q = "from CreditAccount where ID = " + entity.getID();
		long credits = 0;
		try {
			CreditAccount acct = (CreditAccount) loadObject(createQuery(q));
			if(acct != null)
				credits = acct.getCredits();
		}
		catch(Throwable e) {
			log.error(e.getMessage(),e);
		}
		commit();
		return credits;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#getDevelopmentGrant(starcorp.common.entities.Colony,
	 *      starcorp.common.types.AFacilityType, boolean)
	 */
	public DevelopmentGrant getDevelopmentGrant(Colony colony,
			AFacilityType type, boolean openOnly) {
		String q = "from DevelopmentGrant where colony = :colony and type = :type";
		if (openOnly) {
			q = q + " and available = true";
		}
		Map<String, Object> map = prepareParameters("colony", colony);
		prepareParameters(map, "type", type.getKey());
		beginTransaction();
		DevelopmentGrant dev = (DevelopmentGrant) loadObject(createQuery(q, map));
		commit();
		return dev;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#getFacility(starcorp.common.entities.Colony,
	 *      java.lang.Class)
	 */
	public Facility getFacility(Colony colony, Class<?> type) {
		List<AFacilityType> types = AFacilityType.listTypes(type);
		if (types.size() > 0) {
			String q = "from Facility where colony = :colony and type in (";
			for (int i = 0; i < types.size(); i++) {
				if (i > 0)
					q += ", ";
				q += "'" + types.get(i).getKey() + "'";
			}
			q += ")";
			beginTransaction();
			Facility f = (Facility) loadObject(createQuery(q, "colony", colony));
			commit();
			return f;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#getFacility(starcorp.common.entities.Colony,
	 *      starcorp.common.entities.Corporation, java.lang.Class)
	 */
	public Facility getFacility(Colony colony, Corporation owner, Class<?> type) {
		List<AFacilityType> types = AFacilityType.listTypes(type);
		if (types.size() > 0) {
			String q = "from Facility where colony = :colony and owner = :owner and type in (";
			for (int i = 0; i < types.size(); i++) {
				if (i > 0)
					q += ", ";
				q += "'" + types.get(i).getKey() + "'";
			}
			q += ")";
			Map<String, Object> map = prepareParameters("colony", colony);
			prepareParameters(map, "owner", owner);
			beginTransaction();
			Facility f = (Facility) loadObject(createQuery(q, map));
			commit();
			return f;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#getItem(starcorp.common.entities.Colony,
	 *      starcorp.common.types.AItemType)
	 */
	public ColonyItem getItem(Colony colony, AItemType type) {
		String q = "from ColonyItem where colony = :colony and item.type = :type";
		Map<String, Object> map = prepareParameters("colony", colony);
		prepareParameters(map, "type", type.getKey());
		beginTransaction();
		ColonyItem i = (ColonyItem) loadObject(createQuery(q, map));
		commit();
		return i;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#getItem(starcorp.common.entities.Colony,
	 *      starcorp.common.entities.Corporation,
	 *      starcorp.common.types.AItemType)
	 */
	public ColonyItem getItem(Colony colony, Corporation owner, AItemType type) {
		String q = "from ColonyItem where colony = :colony and owner = :owner and item.type = :type";
		Map<String, Object> map = prepareParameters("colony", colony);
		prepareParameters(map, "owner", owner);
		prepareParameters(map, "type", type.getKey());
		beginTransaction();
		ColonyItem i = (ColonyItem) loadObject(createQuery(q, map));
		commit();
		return i;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#getLease(starcorp.common.entities.Colony,
	 *      starcorp.common.entities.Corporation,
	 *      starcorp.common.types.AFacilityType, boolean)
	 */
	public FacilityLease getLease(Colony colony, Corporation owner,
			AFacilityType type, boolean openOnly) {
		String q = "from FacilityLease where colony = :colony and owner = :owner and type = :type";
		if (openOnly) {
			q = q + " and available = true";
		}
		Map<String, Object> map = prepareParameters("colony", colony);
		prepareParameters(map, "owner", owner);
		prepareParameters(map, "type", type.getKey());
		beginTransaction();
		FacilityLease lease = (FacilityLease) loadObject(createQuery(q, map));
		commit();
		return lease;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#getUnemployed(starcorp.common.entities.Colony,
	 *      starcorp.common.types.PopulationClass)
	 */
	public Unemployed getUnemployed(Colony colony, PopulationClass popClass) {
		String q = "from Unemployed where colony = :colony and popClassType = :popClass";
		Map<String, Object> map = prepareParameters("colony", colony);
		prepareParameters(map, "popClass", popClass.getKey());
		beginTransaction();
		Unemployed u = (Unemployed) loadObject(createQuery(q, map));
		commit();
		return u;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#getWorkers(starcorp.common.entities.Facility,
	 *      starcorp.common.types.PopulationClass)
	 */
	public Workers getWorkers(Facility facility, PopulationClass popClass) {
		String q = "from Workers where facility = :facility and popClassType = :popClass";
		Map<String, Object> map = prepareParameters("facility", facility);
		prepareParameters(map, "popClass", popClass.getKey());
		beginTransaction();
		Workers w = (Workers) loadObject(createQuery(q, map));
		commit();
		return w;
	}

	public List<CreditAccount> listAccounts() {
		String q = "from CreditAccount";
		beginTransaction();
		return copyAccounts(listObject(createQuery(q)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listColonies()
	 */
	public List<Colony> listColonies() {
		beginTransaction();
		String q = "from Colony";
		return copyColonies(listObject(createQuery(q)));
	}

	public List<Colony> listColonies(Corporation govt) {
		String q = "from Colony where government = :govt";
		beginTransaction();
		return copyColonies(listObject(createQuery(q, "govt", govt)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listColonies(starcorp.common.entities.Planet)
	 */
	public List<Colony> listColonies(Planet planet) {
		beginTransaction();
		String q = "from Colony where planetID = " + planet.getID();
		return copyColonies(listObject(createQuery(q)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listColonies(starcorp.common.entities.StarSystem)
	 */
	public List<Colony> listColonies(StarSystem excludeSystem) {
		String q = "from Colony as col where col.planetID IN (select ID from Planet as p where p.systemID <> :systemID)";
		beginTransaction();
		return copyColonies(listObject(createQuery(q, "systemID", excludeSystem.getID())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listColonies(starcorp.common.entities.StarSystem,
	 *      starcorp.common.types.CoordinatesPolar)
	 */
	public List<Colony> listColonies(StarSystem system,
			CoordinatesPolar excludeLocation) {
		String q = "from Colony as col where col.planetID IN (select ID from Planet as p where p.location <> :location and p.systemID = " + system.getID() +")";
		beginTransaction();
		return copyColonies(listObject(createQuery(q, "location", excludeLocation)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listColonies(starcorp.common.entities.StarSystem,
	 *      starcorp.common.types.CoordinatesPolar,
	 *      starcorp.common.entities.Planet)
	 */
	public List<Colony> listColonies(StarSystem system,
			CoordinatesPolar location, Planet excludePlanet) {
		String q = "from Colony as col where col.planetID IN " + 
		"(select ID from Planet as p where p.ID <> :planetId and p.location <> :location and p.systemID = " + system.getID() + ")";
		Map<String, Object> map = prepareParameters("planetId", excludePlanet
				.getID());
		prepareParameters(map, "location", location);
		beginTransaction();
		return copyColonies(listObject(createQuery(q, map)));
	}

	public List<ColonistGrant> listColonistGrants(Colony colony,
			boolean openOnly) {
		beginTransaction();
		return copyGrants(listObject(queryColonistGrants(colony, openOnly)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listColonistGrants(starcorp.common.entities.Corporation,
	 *      boolean)
	 */
	public List<ColonistGrant> listColonistGrants(Corporation owner,
			boolean openOnly) {
		String q = "from ColonistGrant grant left join grant.colony as col where col.government = :owner";
		if (openOnly) {
			q = q + " and available = true";
		}
		Map<String, Object> map = prepareParameters("owner", owner);
		beginTransaction();
		return copyGrants(listObject(createQuery(q, map)));
	}

	public List<AColonists> listColonists() {
		String q = "from AColonists";
		beginTransaction();
		return copyColonists(listObject(createQuery(q)));
	}

	// private List<StarSystem> copyStars(List<?> objects) {
	// List<StarSystem> list = new ArrayList<StarSystem>();
	// for(Object o : objects) {
	// list.add((StarSystem)o);
	// }
	// commit();
	// return list;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listColonists(starcorp.common.entities.Colony)
	 */
	public List<AColonists> listColonists(Colony colony) {
		String q = "from AColonists where colony = :colony";
		beginTransaction();
		return copyColonists(listObject(createQuery(q, "colony", colony)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listColonists(starcorp.common.entities.Colony,
	 *      starcorp.common.types.PopulationClass)
	 */
	public List<AColonists> listColonists(Colony colony,
			PopulationClass popClass) {
		String q = "from AColonists where colony = :colony and popClassType = '"
				+ popClass.getKey() + "'";
		beginTransaction();
		return copyColonists(listObject(createQuery(q, "colony", colony)));
	}

	public List<ResourceDeposit> listDeposits(long planetID,
			Coordinates2D location) {
		String q = "from ResourceDeposit where systemEntityID = " +
		planetID + " and location = :location";
		beginTransaction();
		return copyDeposits(listObject(createQuery(q, "location", location)));
	}
	
	public List<ResourceDeposit> listDeposits(Planet planet,
			List<AItemType> types, int minTotal) {
		String q = "from ResourceDeposit where systemEntityID = " + planet.getID() + " and totalQuantity >= "
				+ minTotal;
		int max = types == null ? 0 : types.size();
		if (max > 0) {
			q += " and type IN (";
		}
		for (int i = 0; i < max; i++) {
			if (i > 0)
				q += ", ";
			q += "'" + types.get(i).getKey() + "'";
		}
		if (max > 0) {
			q += ")";
		}
		q += " order by yield desc";
		beginTransaction();
		return copyDeposits(listObject(createQuery(q)));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listDeposits(starcorp.common.entities.StarSystemEntity)
	 */
	public List<ResourceDeposit> listDeposits(StarSystemEntity systemEntity) {
		String q = "from ResourceDeposit where systemEntityID = " + systemEntity.getID();
		beginTransaction();
		return copyDeposits(listObject(createQuery(q)));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listDesigns(starcorp.common.entities.Corporation)
	 */
	public List<StarshipDesign> listDesigns(Corporation owner) {
		String q = "from StarshipDesign where owner = :owner";
		beginTransaction();
		return copyDesigns(listObject(createQuery(q, "owner", owner)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listDevelopmentGrants(starcorp.common.entities.Corporation,
	 *      boolean)
	 */
	public List<DevelopmentGrant> listDevelopmentGrants(Corporation owner,
			boolean openOnly) {
		String q = "from DevelopmentGrant where colony.government = :owner";
		if (openOnly) {
			q = q + " and available = true";
		}
		beginTransaction();
		return copyDevGrants(listObject(createQuery(q, "owner", owner)));
	}

	public List<Object> listEntities(Class<?> entityClass) {
		String q = "from " + entityClass.getSimpleName();
		beginTransaction();
		return copyObjects(listObject(createQuery(q)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listFacilities()
	 */
	public List<Facility> listFacilities() {
		String q = "from Facility";
		beginTransaction();
		return copyFacilities(listObject(createQuery(q)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listFacilities(starcorp.common.entities.Colony)
	 */
	public List<Facility> listFacilities(Colony colony) {
		String q = "from Facility where colony = :colony";
		beginTransaction();
		return copyFacilities(listObject(createQuery(q, "colony", colony)));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listFacilities(starcorp.common.entities.Colony,
	 *      java.lang.Class)
	 */
	public List<Facility> listFacilities(Colony colony, Class<?> type) {
		List<AFacilityType> types = AFacilityType.listTypes(type);
		String q = "from Facility where colony = :colony and type in (";
		for (int i = 0; i < types.size(); i++) {
			if (i > 0)
				q += ", ";
			q += "'" + types.get(i).getKey() + "'";
		}
		q += ")";
		beginTransaction();
		return copyFacilities(listObject(createQuery(q, "colony", colony)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listFacilities(starcorp.common.entities.Corporation)
	 */
	public List<Facility> listFacilities(Corporation owner) {
		String q = "from Facility where owner = :owner";
		beginTransaction();
		return copyFacilities(listObject(createQuery(q, "owner", owner)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listFacilitiesBySalary(starcorp.common.entities.Colony,
	 *      starcorp.common.types.PopulationClass)
	 */
	public List<Facility> listFacilitiesBySalary(PopulationClass popClass) {
		String q = "select facility from Workers as w where w.popClassType = '"
				+ popClass.getKey() + "' order by salary desc";
		beginTransaction();
		return copyFacilities(listObject(createQuery(q)));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listItems(starcorp.common.entities.Corporation)
	 */
	public List<ColonyItem> listItems(Corporation owner) {
		String q = "from ColonyItem where owner = :owner";
		beginTransaction();
		return copyItems(listObject(createQuery(q, "owner", owner)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listItems(starcorp.common.entities.Corporation,
	 *      starcorp.common.entities.Colony, java.util.List)
	 */
	public List<ColonyItem> listItems(Corporation owner, Colony colony,
			List<AItemType> types) {
		String q = "from ColonyItem where owner = :owner and item.type in (";
		for (int i = 0; i < types.size(); i++) {
			if (i > 0)
				q += ", ";
			q += "'" + types.get(i).getKey() + "'";
		}
		q += ")";
		beginTransaction();
		return copyItems(listObject(createQuery(q, "owner", owner)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listLeases(starcorp.common.entities.Corporation,
	 *      boolean)
	 */
	public List<FacilityLease> listLeases(Corporation corp, boolean openOnly) {
		String q = "from FacilityLease where licensee = :corp";
		if (openOnly) {
			q += " and available = true";
		}
		beginTransaction();
		return copyLeases(listObject(createQuery(q, "corp", corp)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listMarket(starcorp.common.entities.Colony,
	 *      int)
	 */
	public List<MarketItem> listMarket(Colony colony, int minQty) {
		String q = "from MarketItem where colony = :colony and item.quantity >= "
				+ minQty;
		beginTransaction();
		return copyMarket(listObject(createQuery(q, "colony", colony)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listMarket(starcorp.common.entities.Colony,
	 *      java.util.List, int)
	 */
	public List<MarketItem> listMarket(Colony colony, List<AItemType> types,
			int minQty) {
		beginTransaction();
		return copyMarket(listObject(queryMarket(colony, types, minQty)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listMarket(int)
	 */
	public List<MarketItem> listMarket(int minQty) {
		String q = "from MarketItem where item.quantity >= :minQty";
		beginTransaction();
		return copyMarket(listObject(createQuery(q, "minQty", minQty)));
	}

	public List<Planet> listPlanets(StarSystem star, int maxGravity,
			List<AtmosphereType> atmospheres) {
		String q = "from Planet where systemID = " + star.getID() +" and gravityRating <= " + maxGravity;
		int max = atmospheres == null ? 0 : atmospheres.size();
		if (max > 0) {
			q += " and atmosphereType in (";
		}
		for (int i = 0; i < max; i++) {
			if (i > 0)
				q += ", ";
			q += "'" + atmospheres.get(i).getKey() + "'";
		}
		if (max > 0) {
			q += ")";
		}
		beginTransaction();
		return copyPlanets(listObject(createQuery(q)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listShips(starcorp.common.entities.Colony)
	 */
	public List<Starship> listShips(Colony docked) {
		String q = "from Starship where colony = :docked";
		beginTransaction();
		return copyShips(listObject(createQuery(q, "docked", docked)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listShips(starcorp.common.entities.Corporation)
	 */
	public List<Starship> listShips(Corporation owner) {
		String q = "from Starship where owner = :owner";
		beginTransaction();
		return copyShips(listObject(createQuery(q, "owner", owner)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listShips(starcorp.common.entities.Planet)
	 */
	public List<Starship> listShips(Planet orbiting) {
		String q = "from Starship where planet = :orbiting and planetLocation.x < 1 and planetLocation.y < 1";
		beginTransaction();
		return copyShips(listObject(createQuery(q, "orbiting", orbiting)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listShips(starcorp.common.entities.Planet,
	 *      starcorp.common.types.Coordinates2D)
	 */
	public List<Starship> listShips(Planet planet, Coordinates2D location) {
		String q = "from Starship where planet = :planet and planetLocation = :location";
		Map<String, Object> map = prepareParameters("planet", planet);
		prepareParameters(map, "location", location);
		beginTransaction();
		return copyShips(listObject(createQuery(q, map)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listSystemEntities(starcorp.common.entities.StarSystem)
	 */
	public List<StarSystemEntity> listSystemEntities(StarSystem star) {
		String q = "from StarSystemEntity where systemID = " + star.getID();
		beginTransaction();
		return copySystemEntities(listObject(createQuery(q)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listSystemEntities(starcorp.common.entities.StarSystem,
	 *      starcorp.common.types.CoordinatesPolar)
	 */
	public List<StarSystemEntity> listSystemEntities(StarSystem star,
			CoordinatesPolar location) {
		String q = "from StarSystemEntity where systemID = " + star.getID() +" and location = :location";
		beginTransaction();
		return copySystemEntities(listObject(createQuery(q, "location", location)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listSystems(starcorp.common.types.Coordinates3D,
	 *      int)
	 */
	public List<StarSystem> listSystems(Coordinates3D origin, int range) {
		String q = "from StarSystem where location <> :location";
		beginTransaction();
		List<?> systems = listObject(createQuery(q, "location", origin));
		List<StarSystem> result = new ArrayList<StarSystem>();
		Iterator<?> i = systems.iterator();
		while (i.hasNext()) {
			StarSystem ss = (StarSystem) i.next();
			int distance = ss.getLocation().getDistance(origin);
			if (distance <= range)
				result.add(ss);
		}
		commit();
		return result;
	}

	public List<AColonists> listUnemployed() {
		String q = "from Unemployed where quantity > 0";
		beginTransaction();
		return copyColonists(listObject(createQuery(q)));
	}

	public List<AColonists> listUnemployed(Colony colony) {
		String q = "from Unemployed where colony = :colony";
		beginTransaction();
		return copyColonists(listObject(createQuery(q, "colony", colony)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listWorkers(starcorp.common.entities.Colony)
	 */
	public List<AColonists> listWorkers(Colony colony) {
		String q = "from Workers where colony = :colony";
		beginTransaction();
		return copyColonists(listObject(createQuery(q, "colony", colony)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listWorkers(starcorp.common.entities.Colony,
	 *      starcorp.common.types.PopulationClass)
	 */
	public List<AColonists> listWorkers(Colony colony, PopulationClass popClass) {
		String q = "from Workers where colony = :colony and popClassType = '"
				+ popClass.getKey() + "'";
		beginTransaction();
		return copyColonists(listObject(createQuery(q, "colony", colony)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listWorkers(starcorp.common.entities.Facility)
	 */
	public List<AColonists> listWorkers(Facility facility) {
		String q = "from Workers where facility = :facility";
		beginTransaction();
		return copyColonists((listObject(createQuery(q, "facility", facility))));
	}

	public IEntity load(Class<?> entityClass, long ID) {
		beginTransaction();
		IEntity entity = (IEntity) loadObject(entityClass, ID);
		commit();
		return entity;
	}

	public Map<PopulationClass, ColonistGrant> mapColonistGrantsByPopClass(
			Colony colony, boolean openOnly) {
		beginTransaction();
		Map<PopulationClass, ColonistGrant> map = new HashMap<PopulationClass, ColonistGrant>();
		Query q = queryColonistGrants(colony, openOnly);
		for(Object o : listObject(q)) {
			ColonistGrant grant = (ColonistGrant) o;
			map.put(grant.getPopClass(), grant);
		}
		commit();
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see starcorp.server.entitystore.IEntityStore#listFacilitiesWithWorkers(starcorp.common.entities.Colony,
	 *      java.util.List)
	 */
	public Map<Facility, List<AColonists>> mapFacilitiesWithWorkers(
			Colony colony, List<AFacilityType> types) {
		Map<Facility, List<AColonists>> result = new HashMap<Facility, List<AColonists>>();
		
		beginTransaction();
		List<?> workers = listObject(queryFacilitiesByType(colony, types));
		for(Object o : workers) {
			Workers w = (Workers) o;
			List<AColonists> list = result.get(w.getFacility());
			if(list == null) {
				list = new ArrayList<AColonists>();
			}
			list.add(w);
			result.put(w.getFacility(), list);
		}
		commit();
		return result;
	}

	public Map<AFacilityType, Map<Facility, List<AColonists>>> mapFacilitiesWithWorkersByType(
			Colony colony, List<AFacilityType> types) {
		Map<AFacilityType, Map<Facility, List<AColonists>>> result = new HashMap<AFacilityType, Map<Facility,List<AColonists>>>();
		
		beginTransaction();
		List<?> workers = listObject(queryFacilitiesByType(colony, types));
		for(Object o : workers) {
			Workers w = (Workers) o;
			AFacilityType ft = w.getFacility().getTypeClass();
			Map<Facility, List<AColonists>> m = result.get(ft);
			if(m == null) {
				m = new HashMap<Facility, List<AColonists>>();
				result.put(ft,m);
			}
			List<AColonists> list = m.get(w.getFacility());
			if(list == null) {
				list = new ArrayList<AColonists>();
			}
			list.add(w);
			m.put(w.getFacility(), list);
		}
		commit();
		return result;
	}

	public Map<AItemType, List<MarketItem>> mapMarketByItemType(Colony colony,
			List<AItemType> types) {
		Map<AItemType, List<MarketItem>> market = new HashMap<AItemType, List<MarketItem>>();
		beginTransaction();
		List<?> marketItems = listObject(queryMarket(colony, types, 1));
		for(Object o : marketItems) {
			MarketItem item = (MarketItem)o;
			List<MarketItem> list = market.get(item.getItem().getTypeClass());
			if(list == null) {
				list = new ArrayList<MarketItem>();
			}
			list.add(item);
			market.put(item.getItem().getTypeClass(),list);
		}
		commit();
		return market;
	}

	public List<Object> query(String hql) {
		beginTransaction();
		return copyObjects(listObject(createQuery(hql)));
	}

	public long removeCredits(IEntity entity, long credits, String reason) {
		addCredits(entity, (0 - credits), reason);
		return getCredits(entity);
	}

	public void shutdown() {
		sessionFactory.close();
	}

	public long transferCredits(IEntity from, IEntity to, long credits,
			String reason) {
		long total = addCredits(to, credits, reason);
		removeCredits(from, credits, reason);
		return total;
	}

	public IEntity update(IEntity entity) {
		beginTransaction();
		entity = (IEntity) getSession().merge(entity);
		commit();
		return entity;
	}

	public AGovernmentLaw update(AGovernmentLaw law) {
		beginTransaction();
		getSession().merge(law);
		commit();
		return law;
	}

	public ACorporateItem update(ACorporateItem item) {
		beginTransaction();
		getSession().merge(item);
		commit();
		return item;
	}

	public ResourceDeposit update(ResourceDeposit deposit) {
		beginTransaction();
		getSession().merge(deposit);
		commit();
		return deposit;
	}

	public List<Facility> listFacilitiesPowered(List<AFacilityType> types) {
		String q = "from Facility where powered = true";
		if(types != null && types.size() > 0) {
			q += " and type IN (";
			int i = 0;
			for(AFacilityType type : types) {
				if(i > 0)
					q += ", ";
				q += "'" + type.getKey() + "'";
				i++;
			}
			q += ")";
		}
		beginTransaction();
		return copyFacilities(listObject(createQuery(q)));
	}

	public double getAverageHappiness(Colony colony, PopulationClass popClass) {
		String q = "select avg(col.happiness) from AColonists as col where col.colony = :colony and col.popClassType = '" + popClass.getKey() + "'";
		beginTransaction();
		Query query = createQuery(q, "colony", colony);
		double avg = 0.0;
		try {
			avg = (Double) query.uniqueResult();
		}
		catch(Throwable e) {
			log.error(e.getMessage(),e);
		}
		commit();
		return avg;
	}
	
	private List<FactoryQueueItem> copyQueue(List<?> objects) {
		List<FactoryQueueItem> list = new ArrayList<FactoryQueueItem>();
		for(Object o : objects) {
			list.add((FactoryQueueItem)o);
		}
		return list;
	}

	public List<FactoryQueueItem> listQueue(Facility facility) {
		String q = "from FactoryQueueItem where factory = :facility order by position";
		beginTransaction();
		return copyQueue(listObject(createQuery(q, "facility", facility)));
	}

	public double getAveragePrice(Colony colony, AItemType type) {
		if(type == null || colony == null)
			return 0.0;
		String q = "select avg(costPerItem) from MarketItem where item.type = '" + type.getKey() +"' and colony = :colony";
		beginTransaction();
		Query query = createQuery(q, "colony", colony);
		double avg = 0.0;
		try {
			avg = (Double) query.uniqueResult();
		}
		catch(Throwable e) {
			log.error(e.getMessage(),e);
		}
		commit();
		return avg;
	}

}
