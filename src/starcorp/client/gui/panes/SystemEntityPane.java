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
package starcorp.client.gui.panes;

import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Widget;

import starcorp.client.gui.AEntityPane;
import starcorp.client.gui.windows.MainWindow;
import starcorp.common.entities.Colony;
import starcorp.common.entities.Planet;
import starcorp.common.entities.StarSystemEntity;
import starcorp.common.types.PlanetMapSquare;
import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

/**
 * starcorp.client.gui.SystemEntityPane
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 26 Sep 2007
 */
public class SystemEntityPane extends AEntityPane {

	private final StarSystemEntity entity;
	
	public SystemEntityPane(MainWindow mainWindow,StarSystemEntity entity) {
		super(mainWindow,entity);
		this.entity = entity;
	}

	/* (non-Javadoc)
	 * @see starcorp.client.gui.ADataPane#createWidgets(java.util.List)
	 */
	@Override
	protected void createWidgets(List<Widget> widgets) {
		super.createWidgets(widgets);
		
		createLabel(getParent(), widgets, "System:");
		// TODO replace with name + ID
		createLabel(getParent(), widgets, String.valueOf(entity.getSystemID()));
		
		createLabel(getParent(), widgets, "Location:");
		createLabel(getParent(), widgets, entity.getLocation().toString());
		
		createLabel(getParent(),widgets, "Type:");
		if(entity instanceof Planet) {
			createLabel(getParent(), widgets, "Planet");
		}
		else if(entity.isAsteroid()) {
			createLabel(getParent(), widgets, "Asteroid");
		}
		else if(entity.isGasfield()) {
			createLabel(getParent(), widgets, "Gas Field");
		}
		
		if(entity instanceof Planet) {
			Planet planet = (Planet) entity;
			if(planet.getOrbitingID() > 0) {
				// TODO replace with name + ID
				createLabel(getParent(), widgets, "Orbiting:");
				createLabel(getParent(), widgets, String.valueOf(planet.getOrbitingID()));
			}
			createLabel(getParent(),widgets,"Atmosphere:");
			createLabel(getParent(), widgets, planet.getAtmosphereTypeClass().getName());
			
			createLabel(getParent(),widgets,"Base Hazard Level:");
			createLabel(getParent(), widgets, String.valueOf(planet.getAtmosphereTypeClass().getHazardLevel()));
			
			createLabel(getParent(), widgets, "Gravity:");
			createLabel(getParent(), widgets, planet.getGravityRating() + "g");
			
			Set<PlanetMapSquare> squares = planet.getMap();
			if(squares == null || squares.size() > 0) {
				createPlanetMapLink(getParent(), widgets, planet, "View Map");
			}
			
			Set<Colony> colonies = mainWindow.getTurnReport().getKnownColonies(planet);
			if(colonies != null && colonies.size() > 0) {
				Group grp = createGroup(getParent(), widgets, "Colonies");
				grp.setLayout(new GridLayout(3,true));
				grp.setLayoutData(new GridData(SWT.DEFAULT,SWT.DEFAULT,true,true,2,1));
				for(Colony colony : colonies) {
					createColonyLink(grp, widgets, colony, null);
				}
			}
		}
	}

}
