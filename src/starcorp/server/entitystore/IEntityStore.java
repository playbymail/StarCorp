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
 * starcorp.server.entitystore.IEntityStore
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public interface IEntityStore {

	public static class EntityStoreException extends RuntimeException {

		private static final long serialVersionUID = 5228171587307250006L;

		public EntityStoreException() {
			super();
		}

		public EntityStoreException(String msg, Throwable e) {
			super(msg, e);
		}

		public EntityStoreException(String msg) {
			super(msg);
		}

		public EntityStoreException(Throwable e) {
			super(e);
		}
		
	}
	
	public abstract ABaseEntity load(Class<?> entityClass, int ID);
	public abstract ABaseEntity save(ABaseEntity entity);
	
	public abstract List<?> listSystems(Coordinates3D origin, int range);
	
	public abstract List<?> listSystemEntities(StarSystem star);
	public abstract List<?> listSystemEntities(StarSystem star, CoordinatesPolar location);

	public abstract List<?> listColonistGrants(Corporation owner, boolean openOnly);
	public abstract ColonistGrant getColonistGrant(Colony colony, PopulationClass popClass, boolean openOnly);
	
	public abstract List<?> listColonies();
	public abstract List<?> listColonies(Planet planet);
	public abstract List<?> listColonies(StarSystem system, CoordinatesPolar location, Planet excludePlanet);
	public abstract List<?> listColonies(StarSystem system, CoordinatesPolar excludeLocation);
	public abstract List<?> listColonies(StarSystem excludeSystem);
	public abstract Colony getColony(Planet planet, Coordinates2D location);
	
	public abstract List<?> listItems(Corporation owner);
	public abstract List<?> listItems(Corporation owner, Colony colony, List<AItemType> types);
	public abstract ColonyItem getItem(Colony colony, AItemType type);
	public abstract ColonyItem getItem(Colony colony, Corporation owner, AItemType type);
	
	public abstract Corporation getCorporation(String email);
	public abstract Corporation getCorporation(String email, String password);

	public abstract List<?> listDevelopmentGrants(Corporation owner, boolean openOnly);
	public abstract DevelopmentGrant getDevelopmentGrant(Colony colony, AFacilityType type, boolean openOnly);
	
	public abstract List<?> listFacilities();
	public abstract List<?> listFacilities(Colony colony);
	public abstract List<?> listFacilities(Corporation owner);
	public abstract List<?> listFacilities(Colony colony, Class<?> type);
	public abstract List<?> listFacilitiesBySalary(Colony colony, PopulationClass popClass);
	public abstract Map<Facility,List<?>> listFacilitiesWithWorkers(Colony colony, List<AFacilityType> types);
	public abstract Facility getFacility(Colony colony, Corporation owner, Class<?> type);
	public abstract Facility getFacility(Colony colony, Class<?> type);
	
	public abstract List<?> listLeases(Corporation corp, boolean openOnly);
	public abstract FacilityLease getLease(Colony colony, Corporation owner, AFacilityType type, boolean openOnly);
	
	public abstract List<?> listMarket(int minQty);
	public abstract List<?> listMarket(Colony colony, int minQty);
	public abstract List<?> listMarket(Colony colony, List<AItemType> types, int minQty);
	
	public abstract List<?> listShips(Corporation owner);
	public abstract List<?> listShips(Planet orbiting);
	public abstract List<?> listShips(Planet planet, Coordinates2D location);
	public abstract List<?> listShips(Colony docked);
	
	public abstract List<?> listDesigns(Corporation owner);
	
	public abstract List<?> listColonists(Colony colony);
	public abstract List<?> listColonists(Colony colony, PopulationClass popClass);
	public abstract List<?> listWorkers(Facility facility);
	public abstract List<?> listWorkers(Colony colony);
	public abstract List<?> listWorkers(Colony colony, PopulationClass popClass);
	public abstract Workers getWorkers(Facility facility, PopulationClass popClass);
	public abstract Unemployed getUnemployed(Colony colony, PopulationClass popClass);
	
	public abstract List<?> listDeposits(AStarSystemEntity systemEntity);
	public abstract List<?> listDeposits(Planet planet, Coordinates2D location);
	
}
