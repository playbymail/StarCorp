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
package starcorp.server.setup.systems;

import java.util.Set;
import starcorp.common.entities.Colony;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.Planet;
import starcorp.common.entities.StarSystem;
import starcorp.common.types.AItemType;
import starcorp.common.types.Coordinates2D;
import starcorp.common.types.Coordinates3D;
import starcorp.common.types.Resources;
import starcorp.server.ServerConfiguration;
import starcorp.server.setup.AColonyTemplate;
import starcorp.server.setup.Util;
import starcorp.server.setup.Util.SuitableLocation;
import starcorp.server.shell.commands.Survey;

/**
 * starcorp.server.setup.systems.Populated
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 21 Sep 2007
 */
public class Populated extends Standard {

	private static final String[] prefixes = new String[17];
	private static final String[] middles = new String[54];
	private static final String[] suffixes = new String[39];
	
	static {
		prefixes[0] = "Fort ";
		prefixes[1] = "Port ";
		prefixes[2] = "Mount ";
		prefixes[3] = "New ";
		prefixes[4] = "Camp ";
		prefixes[5] = "Old ";
		prefixes[6] = "";
		prefixes[7] = "";
		prefixes[8] = "";
		prefixes[9] = "";
		prefixes[10] = "";
		prefixes[11] = "";
		prefixes[12] = "";
		prefixes[13] = "";
		prefixes[14] = "";
		prefixes[15] = "";
		prefixes[16] = "";

		middles[0] = "Green";
		middles[1] = "Loams";
		middles[2] = "Yellow";
		middles[3] = "Blue";
		middles[4] = "Small";
		middles[5] = "Tallen";
		middles[6] = "Hawks";
		middles[7] = "Sparrow";
		middles[8] = "Hammer";
		middles[9] = "Holly";
		middles[10] = "Blooming";
		middles[11] = "Rune";
		middles[12] = "Silver";
		middles[13] = "Potters";
		middles[14] = "Dark";
		middles[15] = "Red";
		middles[16] = "Golden";
		middles[17] = "Gold";
		middles[18] = "Ice";
		middles[19] = "Hollow";
		middles[20] = "Lan";
		middles[21] = "Os";
		middles[22] = "Allans";
		middles[23] = "Timber";
		middles[24] = "Fallen";
		middles[25] = "Strat";
		middles[26] = "Hali";
		middles[27] = "Eagle";
		middles[28] = "Pine";
		middles[29] = "Apple";
		middles[30] = "By";
		middles[31] = "York";
		middles[32] = "Earth";
		middles[33] = "Wind";
		middles[34] = "Fire";
		middles[35] = "Sod";
		middles[36] = "Sandy";
		middles[37] = "Beds";
		middles[38] = "Kent";
		middles[39] = "Jutes";
		middles[40] = "Wednes";
		middles[41] = "Oswald";
		middles[42] = "Habrig";
		middles[43] = "Kedal";
		middles[44] = "Gantrick";
		middles[45] = "Jonas";
		middles[46] = "Ossic";
		middles[47] = "Sid";
		middles[48] = "Pound";
		middles[49] = "Sliccic";
		middles[50] = "Brase";
		middles[51] = "Ash";
		middles[52] = "Wild";
		middles[53] = "Tree";
		middles[53] = "Windy";

		suffixes[0] = "burgh";
		suffixes[1] = "vale";
		suffixes[2] = "down";
		suffixes[3] = " River";
		suffixes[4] = " Valley";
		suffixes[5] = " River Valley";
		suffixes[6] = "hill";
		suffixes[7] = "berg";
		suffixes[8] = "dale";
		suffixes[9] = "shine";
		suffixes[10] = "grove";
		suffixes[11] = "bere";
		suffixes[12] = "ton";
		suffixes[13] = "";
		suffixes[14] = "wick";
		suffixes[15] = "ford";
		suffixes[16] = "ham";
		suffixes[17] = "caster";
		suffixes[18] = "mouth";
		suffixes[19] = "vasser";
		suffixes[20] = " Breeze";
		suffixes[21] = "-on-Naze";
		suffixes[22] = "mountain";
		suffixes[23] = "berry";
		suffixes[24] = "fax";
		suffixes[25] = "bee";
		suffixes[26] = " Grotto";
		suffixes[27] = "borough";
		suffixes[28] = "wing";
		suffixes[29] = "shaft";
		suffixes[30] = "water";
		suffixes[31] = "claw";
		suffixes[32] = "don";
		suffixes[33] = "tol";
		suffixes[34] = "bottom";
		suffixes[35] = "tol";
		suffixes[36] = "point";
		suffixes[37] = "warren";
		suffixes[38] = " Gulch";
	}
	
	private static String getRandomName() {
		return prefixes[Util.rnd.nextInt(prefixes.length)] +
		middles[Util.rnd.nextInt(middles.length)] +
		suffixes[Util.rnd.nextInt(suffixes.length)];
	}
	
	@Override
	public StarSystem create(Coordinates3D location, String name) {
		StarSystem system = super.create(location, name);
		Corporation govt = new Corporation();
		govt.setCredits(10000000);
		govt.setFoundedDate(ServerConfiguration.getCurrentDate());
		govt.setName(name + " Federation");
		entityStore.save(govt);
		if(log.isDebugEnabled())
			log.debug("Created " + govt);
		Set<Util.SuitableLocation> suitableLocations = Survey.findSuitableLocations(system, entityStore, AItemType.listTypes(Resources.class));
		boolean capitol = false;
		int cities = 0;
		int towns = 0;
		int outposts = 0;
		for(SuitableLocation loc : suitableLocations) {
			if(!capitol) {
				createColony(govt,loc.planet,loc.location,"Capitol");
				capitol = true;
			}
			else if(cities < 2) {
				createColony(govt,loc.planet,loc.location,"City");
				cities++;
			}
			else if(towns < 3) {
				createColony(govt,loc.planet,loc.location,"Town");
				towns++;
			}
			else if(outposts < 10) {
				createColony(govt,loc.planet,loc.location,"Outpost");
				outposts++;
			}
			else {
				break;
			}
		}
		return system;
	}
	
	
	protected Colony createColony(Corporation govt, Planet planet, Coordinates2D location, String templateName) {
		AColonyTemplate template = AColonyTemplate.getTemplate(templateName);
		template.setEntityStore(entityStore);
		Colony colony = template.create(govt, planet, location, getRandomName());
		if(log.isDebugEnabled())
			log.debug("Created " + colony);
		return colony;
	}

}
