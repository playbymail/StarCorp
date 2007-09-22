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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import starcorp.common.entities.AColonists;
import starcorp.common.entities.ColonistGrant;
import starcorp.common.entities.Colony;
import starcorp.common.entities.Facility;
import starcorp.common.entities.MarketItem;
import starcorp.common.entities.Workers;
import starcorp.common.types.AFacilityType;
import starcorp.common.types.AItemType;
import starcorp.common.types.ConsumerGoods;
import starcorp.common.types.PopulationClass;
import starcorp.common.types.ServiceFacility;
import starcorp.server.engine.AServerTask;

/**
 * starcorp.server.population.PopulationProcessor
 * 
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 17 Sep 2007
 */
public class PopulationProcessor extends AServerTask {

	private static Log log = LogFactory.getLog(PopulationProcessor.class);

	private Vector<ColonyUpdater> updaters = new Vector<ColonyUpdater>(); 
	
	@Override
	protected void doJob() throws Exception {
		log.info("Starting population processor");
		beginTransaction();
		List<Colony> colonies = new ArrayList<Colony>();
		int size = colonies.size();
		int i = 1;
		log.info(this +": " + size + " colonies to process.");
		for (Object o : entityStore.listColonies()) {
			colonies.add((Colony)o);
		}
		commit();
		for(Colony colony : colonies) {
			if (log.isDebugEnabled())
				log.debug(this + ": Scheduling colony updater " + i + " of " + size);
			ColonyUpdater updater = createUpdater(colony);
			updaters.add(updater);
			engine.schedule(updater);
			i++;
		}
		log.info(this + ": Finished scheduling. Waiting for updaters to complete.");
		while(getUpdatersRunning() != 0) {
			Thread.yield();
		}
		log.info(this + ": Updaters finished.  Processing unemployed migration.");
		engine.schedule(new UnemployedMigration());
		log.info("Finished population processor");
	}
	
	private ColonyUpdater createUpdater(Colony colony) {
		Map<AItemType, List<MarketItem>> market = new HashMap<AItemType, List<MarketItem>>();
		Map<AFacilityType, Map<Facility, List<Workers>>> services = new HashMap<AFacilityType, Map<Facility, List<Workers>>>();
		Map<PopulationClass, ColonistGrant> grants = new HashMap<PopulationClass, ColonistGrant>();
		List<AColonists> colonists = new ArrayList<AColonists>();
		beginTransaction();
		List<?> marketItems = entityStore.listMarket(colony, AItemType.listTypes(ConsumerGoods.class), 1);
		for(Object o : marketItems) {
			MarketItem item = (MarketItem) o;
			List<MarketItem> list = market.get(item.getItem().getTypeClass());
			if(list == null) {
				list = new ArrayList<MarketItem>();
			}
			list.add(item);
			market.put(item.getItem().getTypeClass(),list);
		}
		Map<Facility, List<?>> facilities = entityStore.listFacilitiesWithWorkers(colony, AFacilityType.listTypes(ServiceFacility.class));
		for(Facility f : facilities.keySet()) {
			AFacilityType type = f.getTypeClass(); 
			Map<Facility, List<Workers>> map = services.get(type);
			if(map == null) {
				map = new HashMap<Facility, List<Workers>>();
			}
			ArrayList<Workers> workers = new ArrayList<Workers>();
			List<?> list = facilities.get(f);
			for(Object o : list) {
				workers.add((Workers)o);
			}
			map.put(f,workers);
			services.put(type, map);
		}
		List<?> grantsList = entityStore.listColonistGrants(colony, true);
		for(Object o : grantsList) {
			ColonistGrant cg = (ColonistGrant) o;
			grants.put(cg.getPopClass(),cg);
		}
		for(Object o : entityStore.listColonists(colony)) {
			colonists.add((AColonists)o);
		}
		commit();
		return new ColonyUpdater(colony,market,services,grants,colonists,this);
	}

	void done(ColonyUpdater updater) {
		updaters.remove(updater);
	}
	
	public int getUpdatersRunning() {
		return updaters.size();
	}

	@Override
	protected Log getLog() {
		return log;
	}

	@Override
	protected String getName() {
		return "Population Processor [" + getUpdatersRunning() + " updaters running]";
	}


}
