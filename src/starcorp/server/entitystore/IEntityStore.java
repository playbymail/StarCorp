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

import starcorp.common.entities.ABaseEntity;
import starcorp.common.entities.AStarSystemEntity;
import starcorp.common.entities.ColonistGrant;
import starcorp.common.entities.Colony;
import starcorp.common.entities.ColonyItem;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.DevelopmentGrant;
import starcorp.common.entities.Facility;
import starcorp.common.entities.FacilityLease;
import starcorp.common.entities.MarketItem;
import starcorp.common.entities.Planet;
import starcorp.common.entities.StarSystem;
import starcorp.common.entities.Starship;
import starcorp.common.entities.StarshipDesign;
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
	
	public abstract ABaseEntity load(int ID);
	public abstract ABaseEntity save(ABaseEntity entity);
	
	public abstract List<AStarSystemEntity> listSystemEntities(StarSystem star);
	public abstract List<AStarSystemEntity> listSystemEntities(StarSystem star, Coordinates3D location);

	public abstract List<ColonistGrant> listColonistGrants(Corporation owner, boolean openOnly);
	public abstract List<ColonistGrant> listColonistGrants(Colony colony, boolean openOnly);
	public abstract ColonistGrant getColonistGrant(Colony colony, PopulationClass popClass, boolean openOnly);
	
	public abstract List<Colony> listColonies(Planet planet);
	public abstract Colony getColony(Planet planet, Coordinates2D location);
	public abstract List<Colony> listColonies(Corporation owner);
	
	public abstract List<ColonyItem> listItems(Colony colony);
	public abstract List<ColonyItem> listItems(Colony colony, Corporation owner);
	public abstract List<ColonyItem> listItems(Corporation owner);
	public abstract List<ColonyItem> listItems(Corporation owner, Colony colony, Class<?> typeClass);
	public abstract ColonyItem getItem(Colony colony, AItemType type);
	public abstract ColonyItem getItem(Colony colony, Corporation owner, AItemType type);
	public abstract ColonyItem getItem(Corporation owner, AItemType type);
	
	public abstract Corporation getCorporation(String email, String password);

	public abstract List<DevelopmentGrant> listDevelopmentGrants(Corporation owner, boolean openOnly);
	public abstract List<DevelopmentGrant> listDevelopmentGrants(Colony colony, boolean openOnly);
	public abstract DevelopmentGrant getDevelopmentGrant(Colony colony, AFacilityType type, boolean openOnly);
	
	public abstract List<Facility> listFacilities(Colony colony);
	public abstract List<Facility> listFacilities(Colony colony, Corporation owner);
	public abstract List<Facility> listFacilities(Corporation owner);
	public abstract List<Facility> listFacilities(Colony colony, Corporation owner, AFacilityType type);
	public abstract List<Facility> listFacilities(Colony colony, Corporation owner, Class<?> type);
	public abstract List<Facility> listFacilities(Colony colony, Class<?> type);
	public abstract Facility getFacility(Colony colony, Corporation owner, Class<?> type);
	
	public abstract List<FacilityLease> listLeases(Corporation corp, boolean openOnly);
	public abstract List<FacilityLease> listLeases(Colony colony, boolean openOnly);
	public abstract FacilityLease getLease(Colony colony, Corporation owner, AFacilityType type, boolean openOnly);
	public abstract List<FacilityLease> listLeases(Colony colony, Corporation owner, boolean openOnly);
	
	public abstract List<MarketItem> listMarket(int minQty);
	public abstract List<MarketItem> listMarket(Colony colony, int minQty);
	public abstract List<MarketItem> listMarket(Colony colony, Corporation owner, int minQty);
	public abstract List<MarketItem> listMarket(Corporation owner, int minQty);
	
	public abstract List<Starship> listShips(Corporation owner);
	public abstract List<Starship> listShips(StarSystem star);
	public abstract List<Starship> listShips(StarSystem star, CoordinatesPolar location);
	public abstract List<Starship> listShips(Planet orbiting);
	public abstract List<Starship> listShips(Planet planet, Coordinates2D location);
	public abstract List<Starship> listShips(Colony docked);
	
	public abstract List<StarshipDesign> listDesigns(Corporation owner);
	
	
}
