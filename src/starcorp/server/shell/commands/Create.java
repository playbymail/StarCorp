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
package starcorp.server.shell.commands;

import starcorp.common.entities.Colony;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.Facility;
import starcorp.common.entities.Planet;
import starcorp.common.entities.StarSystem;
import starcorp.common.entities.Starship;
import starcorp.common.entities.StarshipDesign;
import starcorp.common.types.AFacilityType;
import starcorp.server.ServerConfiguration;
import starcorp.server.setup.AColonyTemplate;
import starcorp.server.setup.APlanetTemplate;
import starcorp.server.setup.ASystemTemplate;
import starcorp.server.shell.ACommand;

/**
 * starcorp.server.shell.commands.Create
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 20 Sep 2007
 */
public class Create extends ACommand {

	/* (non-Javadoc)
	 * @see starcorp.server.shell.ACommand#getHelpText()
	 */
	@Override
	public String getHelpText() {
		return "create planet (Template) (System ID) (quadrant) (orbit) (Name)\n\nCreates a planet based on the PlanetTemplate at the specified location with the given name.\n\n" +
		"create moon (Template) (Planet ID) (Name)\n\nCreates a planet based on the PlanetTemplate orbiting the specified planet with the given name.\n\n" +
		"create system (Template) (x) (y) (z) (Name)\n\nCreates a new star system on the SystemTemplate at the specified location.\n\n" +
		"create corp (Name) (Credits)\n\nCreates a new corporation.\n\n" +
		"create colony (Template) (Corporation ID) (Planet ID) (x) (y)\n\nCreates a new colony based on the ColonyTemplate? owned by the specified corporation at the specified location.\n\n" +
		"create design (Corporation ID) (Name) (Hull type 1) (Hull type 2) ... (Hull type n)\n\nCreates a new starship design owned by the specified corporation.\n\n" +
		"create ship (Corporation ID) (Design ID) (Colony ID) (Name)\n\nCreates a new starship.\n\n" +
		"create facility (Corporation ID) (Colony ID) (Type)\n\nCreates a new facility.";
	}

	/* (non-Javadoc)
	 * @see starcorp.server.shell.ACommand#getName()
	 */
	@Override
	public String getName() {
		return "create";
	}

	/* (non-Javadoc)
	 * @see starcorp.server.shell.ACommand#process()
	 */
	@Override
	public void process() throws Exception {
		String function = get(0);
		if("planet".equalsIgnoreCase(function)) {
			String templateName = get(1);
			int systemId = getAsInt(2);
			int quadrant = getAsInt(3);
			int orbit = getAsInt(4);
			String name = concat(5);
			APlanetTemplate template = APlanetTemplate.getTemplate(templateName);
			if(template == null) {
				out.println("Unknown template");
			}
			else {
				template.setEntityStore(entityStore);
				Planet p = template.create(systemId, quadrant, orbit, name);
				if(p == null) {
					out.println("Failed.");
				}
				else {
					out.println("Created " + p);
				}
			}
		}
		else if("moon".equalsIgnoreCase(function)) {
			String templateName = get(1);
			int planetId = getAsInt(2);
			String name = concat(3);
			APlanetTemplate template = APlanetTemplate.getTemplate(templateName);
			if(template == null) {
				out.println("Unknown template");
			}
			else {
				template.setEntityStore(entityStore);
				Planet p = template.create(planetId, name);
				if(p == null) {
					out.println("Failed.");
				}
				else {
					out.println("Created " + p);
				}
			}
		}
		else if("system".equalsIgnoreCase(function)) {
			String templateName = get(1);
			int x = getAsInt(2);
			int y = getAsInt(3);
			int z = getAsInt(4);
			String name = concat(5);
			ASystemTemplate template = ASystemTemplate.getTemplate(templateName);
			if(template == null) {
				out.println("Unknown template");
			}
			else {
				template.setEntityStore(entityStore);
				StarSystem s = template.create(x,y,z, name);
				if(s == null) {
					out.println("Failed.");
				}
				else {
					out.println("Created " + s);
				}
			}
		}
		else if("corp".equalsIgnoreCase(function)) {
			Corporation corp = new Corporation();
			corp.setCredits(getAsInt(1));
			corp.setFoundedDate(ServerConfiguration.getCurrentDate());
			corp.setName(concat(2));
			entityStore.save(corp);
			out.println("Created " + corp);
		}
		else if("colony".equalsIgnoreCase(function)) {
			String templateName = get(1);
			int govtId = getAsInt(2);
			int planetId = getAsInt(3);
			int x = getAsInt(4);
			int y = getAsInt(5);
			String name = concat(6);
			AColonyTemplate template = AColonyTemplate.getTemplate(templateName);
			if(template == null) {
				out.println("Unknown template");
			}
			else {
				template.setEntityStore(entityStore);
				Colony c = template.create(govtId,planetId,x,y,name);
				if(c == null) {
					out.println("Failed.");
				}
				else {
					out.println("Created " + c);
				}
			}
		}
		else if("design".equalsIgnoreCase(function)) {
			int corpId = getAsInt(1);
			Corporation corp = (Corporation) entityStore.load(Corporation.class, corpId);
			if(corp != null) {
				String name = get(2);
				StarshipDesign design = new StarshipDesign();
				design.setDesignDate(ServerConfiguration.getCurrentDate());
				design.setName(name);
				design.setOwner(corp);
				for(int i = 3; i < count(); i++) {
					design.addHulls(get(i));
				}
				if(design.isValid()) {
					entityStore.save(design);
					out.println("Created " + design);
				}
				else {
					out.println("Invalid design");
				}
			}
			else {
				out.println("Invalid corporation");
			}
		}
		else if("ship".equalsIgnoreCase(function)) {
			int corpId = getAsInt(1);
			int designId = getAsInt(2);
			int colonyId = getAsInt(3);
			String name = get(4);
			
			Corporation corp = (Corporation) entityStore.load(Corporation.class, corpId);
			StarshipDesign design = (StarshipDesign) entityStore.load(StarshipDesign.class, designId);
			Colony colony = (Colony) entityStore.load(Colony.class, colonyId);
			
			if(corp == null || design == null || colony == null) {
				out.println("Invalid arguments");
			}
			else {
				Starship ship = new Starship();
				ship.setBuiltDate(ServerConfiguration.getCurrentDate());
				ship.setDesign(design);
				ship.setLocation(colony.getPlanet().getLocation());
				ship.setName(name);
				ship.setOwner(corp);
				ship.setPlanet(colony.getPlanet());
				ship.setSystem(colony.getPlanet().getSystem());
				ship.setColony(colony);
				entityStore.save(ship);
				out.println("Created " + ship);
			}
		}
		else if("facility".equalsIgnoreCase(function)) {
			int corpId = getAsInt(1);
			int colonyId = getAsInt(2);
			String typeName = get(3);
			
			Corporation corp = (Corporation) entityStore.load(Corporation.class, corpId);
			Colony colony = (Colony) entityStore.load(Colony.class, colonyId);
			AFacilityType type = AFacilityType.getType(typeName);
			
			if(corp == null || colony == null || type == null) {
				out.println("Invalid arguments");
			}
			else {
				Facility facility = new Facility();
				facility.setBuiltDate(ServerConfiguration.getCurrentDate());
				facility.setColony(colony);
				facility.setOpen(true);
				facility.setOwner(corp);
				facility.setTypeClass(type);
				entityStore.save(facility);
				out.println("Created " + facility);
			}
		}
		else {
			out.println("Invalid arguments.");
		}
		out.flush();

	}

}
