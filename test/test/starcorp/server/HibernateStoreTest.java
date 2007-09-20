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
package test.starcorp.server;

import starcorp.common.entities.ColonistGrant;
import starcorp.common.entities.Colony;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.Planet;
import starcorp.common.entities.StarSystem;
import starcorp.common.types.AtmosphereType;
import starcorp.common.types.Coordinates2D;
import starcorp.common.types.Coordinates3D;
import starcorp.common.types.CoordinatesPolar;
import starcorp.common.types.PlanetMapSquare;
import starcorp.common.types.PopulationClass;
import starcorp.common.types.TerrainType;
import starcorp.server.ServerConfiguration;
import starcorp.server.entitystore.HibernateStore;
import starcorp.server.entitystore.IEntityStore;
import junit.framework.TestCase;

/**
 * test.starcorp.server.HibernateStoreTest
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 19 Sep 2007
 */
public class HibernateStoreTest extends TestCase {

	private IEntityStore entityStore = new HibernateStore();
	
	private PopulationClass admin = PopulationClass.getType("admin");
	private AtmosphereType greenAtmosphere = AtmosphereType.getType("green");
	private TerrainType plainsTerrain = TerrainType.getType("plain");
	
	private Coordinates3D galaxyLocation = new Coordinates3D(1,1,1);
	private CoordinatesPolar systemLocation = new CoordinatesPolar(1,1);
	private Coordinates2D planetLocation = new Coordinates2D(1,1);
	private PlanetMapSquare mapSquare = new PlanetMapSquare(planetLocation,plainsTerrain);
	private StarSystem system;
	private Planet planet;
	private Corporation corp;
	private Colony colony;
	private ColonistGrant grant;
	
	@Override
	protected void setUp() throws Exception {
		
		if(corp == null) {
			corp = new Corporation();
			corp.setCredits(10000000);
			corp.setFoundedDate(ServerConfiguration.getCurrentDate());
			corp.setName("Test Corp");
			corp.setPlayerEmail("test" + System.currentTimeMillis() + "@test.com");
			corp.setPlayerName("Test Player");
			corp.setPlayerPassword("test");
			entityStore.save(corp);
		}
		
		if(system == null) {
			system = new StarSystem();
			system.setLocation(galaxyLocation);
			system.setName("Test System");
			system.setType("M");
			entityStore.save(system);
		}
		
		if(planet == null) {
			planet = new Planet();
			planet.set(mapSquare);
			planet.setAtmosphereTypeClass(greenAtmosphere);
			planet.setGravityRating(1);
			planet.setLocation(systemLocation);
			planet.setName("Test Planet");
			planet.setSystem(system);
			entityStore.save(planet);
		}
		
		if(colony == null) {
			colony = new Colony();
			colony.setFoundedDate(ServerConfiguration.getCurrentDate());
			colony.setGovernment(corp);
			colony.setHazardLevel(planet.getAtmosphereTypeClass().getHazardLevel() + plainsTerrain.getHazardLevel());
			colony.setLocation(planetLocation);
			colony.setName("Test Colony");
			colony.setPlanet(planet);
			entityStore.save(colony);
		}
		
		if(grant == null) {
			grant = new ColonistGrant();
			grant.setAvailable(true);
			grant.setColony(colony);
			grant.setCredits(1000);
			grant.setIssuedDate(ServerConfiguration.getCurrentDate());
			grant.setPopClass(admin);
			entityStore.save(grant);
		}
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#getColonistGrant(starcorp.common.entities.Colony, starcorp.common.types.PopulationClass, boolean)}.
	 */
	public void testGetColonistGrant() {
		
		ColonistGrant grant = entityStore.getColonistGrant(colony, admin, true);
		assertNotNull(grant);
		
		System.out.println(grant);
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#getColony(starcorp.common.entities.Planet, starcorp.common.types.Coordinates2D)}.
	 */
	public void testGetColony() {
		Colony colony = entityStore.getColony(planet, planetLocation);
		assertNotNull(colony);
		
		System.out.println(colony);
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#getCorporation(java.lang.String, java.lang.String)}.
	 */
	public void testGetCorporation() {
		Corporation corp = entityStore.getCorporation(this.corp.getPlayerEmail(), "test");
		assertNotNull(corp);
		
		System.out.println(corp);
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#getDevelopmentGrant(starcorp.common.entities.Colony, starcorp.common.types.AFacilityType, boolean)}.
	 */
	public void testGetDevelopmentGrant() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#getFacility(starcorp.common.entities.Colony, starcorp.common.entities.Corporation, java.lang.Class)}.
	 */
	public void testGetFacilityColonyCorporationClassOfQ() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#getFacility(starcorp.common.entities.Colony, java.lang.Class)}.
	 */
	public void testGetFacilityColonyClassOfQ() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#getItem(starcorp.common.entities.Colony, starcorp.common.types.AItemType)}.
	 */
	public void testGetItemColonyAItemType() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#getItem(starcorp.common.entities.Colony, starcorp.common.entities.Corporation, starcorp.common.types.AItemType)}.
	 */
	public void testGetItemColonyCorporationAItemType() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#getLease(starcorp.common.entities.Colony, starcorp.common.entities.Corporation, starcorp.common.types.AFacilityType, boolean)}.
	 */
	public void testGetLease() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#getUnemployed(starcorp.common.entities.Colony, starcorp.common.types.PopulationClass)}.
	 */
	public void testGetUnemployed() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#getWorkers(starcorp.common.entities.Facility, starcorp.common.types.PopulationClass)}.
	 */
	public void testGetWorkers() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listColonies()}.
	 */
	public void testListColonies() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listColonies(starcorp.common.entities.Planet)}.
	 */
	public void testListColoniesPlanet() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listColonies(starcorp.common.entities.StarSystem, starcorp.common.types.CoordinatesPolar, starcorp.common.entities.Planet)}.
	 */
	public void testListColoniesStarSystemCoordinatesPolarPlanet() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listColonies(starcorp.common.entities.StarSystem, starcorp.common.types.CoordinatesPolar)}.
	 */
	public void testListColoniesStarSystemCoordinatesPolar() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listColonies(starcorp.common.entities.StarSystem)}.
	 */
	public void testListColoniesStarSystem() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listColonistGrants(starcorp.common.entities.Corporation, boolean)}.
	 */
	public void testListColonistGrants() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listColonists(starcorp.common.entities.Colony)}.
	 */
	public void testListColonistsColony() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listColonists(starcorp.common.entities.Colony, starcorp.common.types.PopulationClass)}.
	 */
	public void testListColonistsColonyPopulationClass() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listDeposits(starcorp.common.entities.AStarSystemEntity)}.
	 */
	public void testListDepositsAStarSystemEntity() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listDeposits(starcorp.common.entities.Planet, starcorp.common.types.Coordinates2D)}.
	 */
	public void testListDepositsPlanetCoordinates2D() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listDesigns(starcorp.common.entities.Corporation)}.
	 */
	public void testListDesigns() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listDevelopmentGrants(starcorp.common.entities.Corporation, boolean)}.
	 */
	public void testListDevelopmentGrants() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listFacilities()}.
	 */
	public void testListFacilities() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listFacilities(starcorp.common.entities.Colony)}.
	 */
	public void testListFacilitiesColony() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listFacilities(starcorp.common.entities.Corporation)}.
	 */
	public void testListFacilitiesCorporation() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listFacilities(starcorp.common.entities.Colony, java.lang.Class)}.
	 */
	public void testListFacilitiesColonyClassOfQ() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listFacilitiesBySalary(starcorp.common.entities.Colony, starcorp.common.types.PopulationClass)}.
	 */
	public void testListFacilitiesBySalary() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listFacilitiesWithWorkers(starcorp.common.entities.Colony, java.util.List)}.
	 */
	public void testListFacilitiesWithWorkers() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listItems(starcorp.common.entities.Corporation)}.
	 */
	public void testListItemsCorporation() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listItems(starcorp.common.entities.Corporation, starcorp.common.entities.Colony, java.util.List)}.
	 */
	public void testListItemsCorporationColonyListOfAItemType() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listLeases(starcorp.common.entities.Corporation, boolean)}.
	 */
	public void testListLeases() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listMarket(int)}.
	 */
	public void testListMarketInt() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listMarket(starcorp.common.entities.Colony, int)}.
	 */
	public void testListMarketColonyInt() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listMarket(starcorp.common.entities.Colony, java.util.List, int)}.
	 */
	public void testListMarketColonyListOfAItemTypeInt() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listShips(starcorp.common.entities.Corporation)}.
	 */
	public void testListShipsCorporation() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listShips(starcorp.common.entities.Planet)}.
	 */
	public void testListShipsPlanet() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listShips(starcorp.common.entities.Planet, starcorp.common.types.Coordinates2D)}.
	 */
	public void testListShipsPlanetCoordinates2D() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listShips(starcorp.common.entities.Colony)}.
	 */
	public void testListShipsColony() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listSystemEntities(starcorp.common.entities.StarSystem)}.
	 */
	public void testListSystemEntitiesStarSystem() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listSystemEntities(starcorp.common.entities.StarSystem, starcorp.common.types.CoordinatesPolar)}.
	 */
	public void testListSystemEntitiesStarSystemCoordinatesPolar() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listSystems(starcorp.common.types.Coordinates3D, int)}.
	 */
	public void testListSystems() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listWorkers(starcorp.common.entities.Facility)}.
	 */
	public void testListWorkersFacility() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listWorkers(starcorp.common.entities.Colony)}.
	 */
	public void testListWorkersColony() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link starcorp.server.entitystore.HibernateStore#listWorkers(starcorp.common.entities.Colony, starcorp.common.types.PopulationClass)}.
	 */
	public void testListWorkersColonyPopulationClass() {
		fail("Not yet implemented"); // TODO
	}

}
