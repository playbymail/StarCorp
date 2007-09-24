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

import java.util.List;
import java.util.Map;

import starcorp.common.entities.ABaseEntity;
import starcorp.common.entities.AColonists;
import starcorp.common.entities.ACorporateItem;
import starcorp.common.entities.AGovernmentLaw;
import starcorp.common.entities.CreditAccount;
import starcorp.common.entities.FactoryQueueItem;
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

/**
 * starcorp.server.entitystore.IEntityStore
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public interface IEntityStore {
	public abstract void shutdown();

	public abstract double getAverageHappiness(Colony colony, PopulationClass popClass);
	
	public abstract long getCredits(ABaseEntity entity);
	public abstract long removeCredits(ABaseEntity entity, long credits, String reason);
	public abstract long addCredits(ABaseEntity entity, long credits, String reason);
	public abstract long transferCredits(ABaseEntity from, ABaseEntity to, long credits, String reason);
	public abstract List<CreditAccount> listAccounts();
	
	public abstract ABaseEntity load(Class<?> entityClass, long ID);
	public abstract ABaseEntity create(ABaseEntity entity);
	public abstract ABaseEntity update(ABaseEntity entity);
	public abstract AGovernmentLaw create(AGovernmentLaw law);
	public abstract AGovernmentLaw update(AGovernmentLaw law);
	public abstract ACorporateItem create(ACorporateItem item);
	public abstract ACorporateItem update(ACorporateItem item);
	public abstract ResourceDeposit create(ResourceDeposit deposit);
	public abstract ResourceDeposit update(ResourceDeposit deposit);
	
	public abstract void delete(ABaseEntity entity);
	public abstract void delete(ACorporateItem item);
	
	public abstract List<Object> query(String hql);
	
	public abstract List<Object> listEntities(Class<?> entityClass);
	
	public abstract List<StarSystem> listSystems(Coordinates3D origin, int range);
	
	public abstract List<StarSystemEntity> listSystemEntities(StarSystem star);
	public abstract List<StarSystemEntity> listSystemEntities(StarSystem star, CoordinatesPolar location);

	public abstract List<Planet> listPlanets(StarSystem star, int maxGravity, List<AtmosphereType> atmospheres);
	
	public abstract List<ColonistGrant> listColonistGrants(Corporation owner, boolean openOnly);
	public abstract List<ColonistGrant> listColonistGrants(Colony colony, boolean openOnly);
	public abstract AGovernmentLaw getColonistGrant(Colony colony, PopulationClass popClass, boolean openOnly);
	
	public abstract List<Colony> listColonies();
	public abstract List<Colony> listColonies(Corporation govt);
	public abstract List<Colony> listColonies(Planet planet);
	public abstract List<Colony> listColonies(StarSystem system, CoordinatesPolar location, Planet excludePlanet);
	public abstract List<Colony> listColonies(StarSystem system, CoordinatesPolar excludeLocation);
	public abstract List<Colony> listColonies(StarSystem excludeSystem);
	public abstract Colony getColony(Planet planet, Coordinates2D location);
	
	public abstract List<ColonyItem> listItems(Corporation owner);
	public abstract List<ColonyItem> listItems(Corporation owner, Colony colony, List<AItemType> types);
	public abstract ColonyItem getItem(Colony colony, AItemType type);
	public abstract ColonyItem getItem(Colony colony, Corporation owner, AItemType type);
	
	public abstract List<FactoryQueueItem> listQueue(Facility facility);
	
	public abstract Corporation getCorporation(String email);
	public abstract Corporation getCorporation(String email, String password);

	public abstract List<DevelopmentGrant> listDevelopmentGrants(Corporation owner, boolean openOnly);
	public abstract DevelopmentGrant getDevelopmentGrant(Colony colony, AFacilityType type, boolean openOnly);
	
	public abstract List<Facility> listFacilities();
	public abstract List<Facility> listFacilitiesPowered(List<AFacilityType> types);
	public abstract List<Facility> listFacilities(Colony colony);
	public abstract List<Facility> listFacilities(Corporation owner);
	public abstract List<Facility> listFacilities(Colony colony, Class<?> type);
	public abstract List<Facility> listFacilitiesBySalary(PopulationClass popClass);
	public abstract Map<Facility,List<AColonists>> mapFacilitiesWithWorkers(Colony colony, List<AFacilityType> types);
	public abstract Map<AFacilityType, Map<Facility, List<AColonists>>> mapFacilitiesWithWorkersByType(
			Colony colony, List<AFacilityType> types);
	public abstract Facility getFacility(Colony colony, Corporation owner, Class<?> type);
	public abstract Facility getFacility(Colony colony, Class<?> type);
	
	public abstract List<FacilityLease> listLeases(Corporation corp, boolean openOnly);
	public abstract FacilityLease getLease(Colony colony, Corporation owner, AFacilityType type, boolean openOnly);
	
	public abstract List<MarketItem> listMarket(int minQty);
	public abstract List<MarketItem> listMarket(Colony colony, int minQty);
	public abstract List<MarketItem> listMarket(Colony colony, List<AItemType> types, int minQty);
	
	public abstract List<Starship> listShips(Corporation owner);
	public abstract List<Starship> listShips(Planet orbiting);
	public abstract List<Starship> listShips(Planet planet, Coordinates2D location);
	public abstract List<Starship> listShips(Colony docked);
	
	public abstract List<StarshipDesign> listDesigns(Corporation owner);
	
	public abstract List<AColonists> listColonists();
	public abstract List<AColonists> listColonists(Colony colony);
	public abstract List<AColonists> listColonists(Colony colony, PopulationClass popClass);
	public abstract List<AColonists> listWorkers(Facility facility);
	public abstract List<AColonists> listWorkers(Colony colony);
	public abstract List<AColonists> listWorkers(Colony colony, PopulationClass popClass);
	public abstract Workers getWorkers(Facility facility, PopulationClass popClass);
	public abstract List<AColonists> listUnemployed();
	public abstract List<AColonists> listUnemployed(Colony colony);
	public abstract Unemployed getUnemployed(Colony colony, PopulationClass popClass);
	
	public abstract List<ResourceDeposit> listDeposits(StarSystemEntity systemEntity);
	public abstract List<ResourceDeposit> listDeposits(long planetID, Coordinates2D location);
	public abstract List<ResourceDeposit> listDeposits(Planet planet, List<AItemType> types, int minTotal);
	
	public Map<AItemType, List<MarketItem>> mapMarketByItemType(Colony colony, List<AItemType> types);
	public Map<PopulationClass, ColonistGrant> mapColonistGrantsByPopClass(Colony colony, boolean openOnly);
}
