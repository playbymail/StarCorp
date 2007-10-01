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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Widget;

import starcorp.client.gui.AEntityPane;
import starcorp.client.gui.windows.MainWindow;
import starcorp.common.entities.StarshipDesign;
import starcorp.common.types.Items;

/**
 * starcorp.client.gui.StarshipDesignPane
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 25 Sep 2007
 */
public class StarshipDesignPane extends AEntityPane {

	private StarshipDesign design;
	
	public StarshipDesignPane(MainWindow mainWindow, StarshipDesign design) {
		super(mainWindow, design);
		this.design = design;
	}

	@Override
	protected void createWidgets(List<Widget> widgets) {
		super.createWidgets(widgets);
		
		Group grpData =  createGroup(getParent(), widgets, "Data");
		grpData.setLayout(new GridLayout(2,true));
		
		createLabel(grpData, widgets, "Hulls: " +design.countHulls());
		createLabel(grpData, widgets, "Mass: " +design.getTotalMass() + "mu");
		createLabel(grpData, widgets, "Impulse Speed: " +design.getImpulseSpeed());
		createLabel(grpData, widgets, "Jump Range: " +design.getJumpRange());
		createLabel(grpData, widgets, "Orbit: " +design.getMaxOrbitGravity() + "g");
		createLabel(grpData, widgets, "Dock: " +design.getMaxDockGravity() + "g");
		createLabel(grpData, widgets, "Short Range Scanners: " +(design.canScanStarSystem() ? "yes" : "no"));
		createLabel(grpData, widgets, "Long Range Scanners: " +design.getScanGalaxyRange());

		Group grpCargo = createGroup(grpData, widgets, "Cargo Capacity");
		grpCargo.setLayout(new RowLayout(SWT.VERTICAL));
		GridData data = new GridData();
		data.minimumWidth=250;
		data.verticalAlignment=SWT.TOP;
		grpCargo.setLayoutData(data);
		
		createLabel(grpCargo, widgets, "Consumer: " +design.getConsumerCapacity() + "mu");
		createLabel(grpCargo, widgets, "Industrial: " +design.getIndustrialCapacity() + "mu");
		createLabel(grpCargo, widgets, "Modules: " +design.getModulesCapacity() + "mu");
		createLabel(grpCargo, widgets, "Organics: " +design.getOrganicsCapacity() + "mu");
		createLabel(grpCargo, widgets, "Liquids / Gas: " +design.getLiquidGasCapacity() + "mu");

		Group grpAbilities = createGroup(grpData,widgets,"Special Abilities");
		grpAbilities.setLayout(new FillLayout(SWT.VERTICAL));
		data = new GridData();
		data.minimumWidth=250;
		data.verticalAlignment=SWT.TOP;
		grpAbilities.setLayoutData(data);
		
		if(design.canMineAsteroid()) {
			createLabel(grpAbilities, widgets, "Mine Asteroids");
		}
		
		if(design.canMineGasField()) {
			createLabel(grpAbilities, widgets, "Mine Gas Fields");
		}
		
		if(design.canProbeField()) {
			createLabel(grpAbilities, widgets, "Probe Asteroids / Gas Fields");
		}
		
		if(design.canProbePlanet()) {
			createLabel(grpAbilities, widgets, "Probe Planets");
		}
		
		if(design.hasBioLab()) {
			createLabel(grpAbilities, widgets, "Prospect (Organics)");
		}
		
		if(design.hasPhysicsLab()) {
			createLabel(grpAbilities, widgets, "Prospect (Mineral / Metal / Fissile)");
		}
		
		if(design.hasGeoLab()) {
			createLabel(grpAbilities, widgets, "Prospect (Fuel / Gas / Metal / Liquid)");
		}

		Group grpHulls = createGroup(getParent(),widgets,"Hulls");
		grpHulls.setLayout(new GridLayout(3,true));
		data = new GridData();
		data.minimumWidth=500;
		data.verticalAlignment=SWT.TOP;
		data.horizontalSpan=2;
		grpHulls.setLayoutData(data);

		for(Items item : design.getHulls()) {
			createItemLink(grpHulls, widgets, item, null);
		}
		
		// TODO create build drop down filtered by colonies where sufficient hulls are available
}
}
