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
package starcorp.server.population;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import starcorp.common.entities.AColonists;
import starcorp.common.entities.Colony;
import starcorp.common.entities.Planet;
import starcorp.common.entities.StarSystem;
import starcorp.common.entities.Unemployed;
import starcorp.common.types.CoordinatesPolar;
import starcorp.server.engine.AServerTask;

/**
 * starcorp.server.population.UnemployedMigration
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 22 Sep 2007
 */
public class UnemployedMigration extends AServerTask {
	private static final Log log = LogFactory.getLog(UnemployedMigration.class);
	

	public static final double MIGRATION_DISTANCE_OTHER_SYSTEM = 0.2;
	public static final double MIGRATION_DISTANCE_SAME_LOCATION = 1.0;
	public static final double MIGRATION_DISTANCE_SAME_PLANET = 2.0;
	public static final double MIGRATION_DISTANCE_SAME_SYSTEM = 0.5;

	private void doMigration(Unemployed unemployed) {
		if (unemployed.getQuantity() < 1)
			return;
		Planet planet = (Planet) entityStore.load(Planet.class, unemployed.getColony().getPlanetID());
		StarSystem system = planet == null ? null : (StarSystem) entityStore.load(StarSystem.class, planet.getSystemID());
		CoordinatesPolar location = planet.getLocation();
		
		List<Colony> samePlanet = entityStore.listColonies(planet);
		List<Colony> sameLocation =  entityStore.listColonies(system, location, planet);
		List<Colony> sameSystem =  entityStore.listColonies(system, location);
		List<Colony> others =  entityStore.listColonies(system);
		doMigration(unemployed, samePlanet, MIGRATION_DISTANCE_SAME_PLANET);
		doMigration(unemployed, sameLocation, MIGRATION_DISTANCE_SAME_LOCATION);
		doMigration(unemployed, sameSystem, MIGRATION_DISTANCE_SAME_SYSTEM);
		doMigration(unemployed, others, MIGRATION_DISTANCE_OTHER_SYSTEM);
		
	}

	private void doMigration(Unemployed unemployed, Colony colony,
			double distanceModifier) {
		if (unemployed.getQuantity() < 1)
			return;
		List<AColonists> colonists = entityStore.listColonists(colony, unemployed
				.getPopClass());
		double happiness = AColonists.getAverageHappiness(colonists);
		double migrationRate = unemployed.getHappiness() - happiness;
		migrationRate *= distanceModifier;

		int migrate = (int) (unemployed.getQuantity() * migrationRate);

		if (migrate > 0) {
			long creditsPerPerson = entityStore.getCredits(unemployed) / unemployed.getQuantity();
			long credits = migrate * creditsPerPerson;
			Unemployed other = entityStore.getUnemployed(colony, unemployed
					.getPopClass());
			if (other == null) {
				other = new Unemployed();
				other.setColony(colony);
				other.setHappiness(0.0);
				other.setPopClass(unemployed.getPopClass());
				entityStore.create(other);
			}
			other.addPopulation(migrate);
			unemployed.removePopulation(migrate);
			entityStore.update(other);
			entityStore.update(unemployed);
			entityStore.removeCredits(unemployed, credits, "");
			if (log.isDebugEnabled())
				log.debug(migrate + " of " + unemployed.getPopClass()
						+ " migrated to " + other);
		}
	}

	private void doMigration(Unemployed unemployed, List<Colony> colonies,
			double distanceModifier) {
		if (unemployed.getQuantity() < 1)
			return;
		for (Colony colony : colonies) {
			doMigration(unemployed, colony, distanceModifier);
		}
	}

	/* (non-Javadoc)
	 * @see starcorp.server.engine.AServerTask#doJob()
	 */
	@Override
	protected void doJob() throws Exception {
		List<AColonists> unemployed = entityStore.listUnemployed();
		int size = unemployed.size();
		int i = 1;
		log.info(this + ": " + size + " unemployed to process");
		for(AColonists colonist : unemployed) {
			if(log.isDebugEnabled())
				log.debug(this + ": " + i + " of " + size + " unemployed processing.");
			doMigration((Unemployed)colonist);
			Thread.yield();
			i++;
		}
	}

	/* (non-Javadoc)
	 * @see starcorp.server.engine.AServerTask#getLog()
	 */
	@Override
	protected Log getLog() {
		return log;
	}

}
