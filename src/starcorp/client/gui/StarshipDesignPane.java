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
package starcorp.client.gui;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;

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
		getDataGroup().setLayout(new GridLayout(2,true));
		
		GridData data = new GridData();
		data.minimumWidth=500;
		data.heightHint=100;
		data.verticalAlignment=SWT.TOP;
		data.horizontalSpan=2;
		
		Group grpData = new Group(getDataGroup(),SWT.NONE);
		grpData.setLayout(new GridLayout(2,true));
		grpData.setText("Data");
		grpData.setLayoutData(data);
		widgets.add(grpData);
		
		Label lblData1 = new Label(grpData,SWT.NONE);
		lblData1.setText("Hulls: " +design.countHulls());
		widgets.add(lblData1);

		Label lblData2 = new Label(grpData,SWT.NONE);
		lblData2.setText("Mass: " +design.getTotalMass() + "mu");
		widgets.add(lblData2);
		
		Label lblMove1 = new Label(grpData,SWT.NONE);
		lblMove1.setText("Impulse Speed: " +design.getImpulseSpeed());
		widgets.add(lblMove1);

		Label lblMove2 = new Label(grpData,SWT.NONE);
		lblMove2.setText("Jump Range: " +design.getJumpRange());
		widgets.add(lblMove2);
		
		Label lblGravity1 = new Label(grpData,SWT.NONE);
		lblGravity1.setText("Orbit: " +design.getMaxOrbitGravity() + "g");
		widgets.add(lblGravity1);

		Label lblGravity2 = new Label(grpData,SWT.NONE);
		lblGravity2.setText("Dock: " +design.getMaxDockGravity() + "g");
		widgets.add(lblGravity2);
		
		Label lblScan1 = new Label(grpData,SWT.NONE);
		lblScan1.setText("Short Range Scanners: " +(design.canScanStarSystem() ? "yes" : "no"));
		widgets.add(lblScan1);

		Label lblScan2 = new Label(grpData,SWT.NONE);
		lblScan2.setText("Long Range Scanners: " +design.getScanGalaxyRange());
		widgets.add(lblScan2);

		Group grpCargo = new Group(getDataGroup(),SWT.NONE);
		grpCargo.setText("Cargo Capacity");
		grpCargo.setLayout(new RowLayout(SWT.VERTICAL));
		data = new GridData();
		data.minimumWidth=250;
		data.verticalAlignment=SWT.TOP;
		grpCargo.setLayoutData(data);
		widgets.add(grpCargo);
		
		Label lblCargoCapacity1 = new Label(grpCargo,SWT.NONE);
		lblCargoCapacity1.setText("Consumer: " +design.getConsumerCapacity() + "mu");
		widgets.add(lblCargoCapacity1);
		
		Label lblCargoCapacity2 = new Label(grpCargo,SWT.NONE);
		lblCargoCapacity2.setText("Industrial: " +design.getIndustrialCapacity() + "mu");
		widgets.add(lblCargoCapacity2);

		Label lblCargoCapacity3 = new Label(grpCargo,SWT.NONE);
		lblCargoCapacity3.setText("Modules: " +design.getModulesCapacity() + "mu");
		widgets.add(lblCargoCapacity3);

		Label lblCargoCapacity4 = new Label(grpCargo,SWT.NONE);
		lblCargoCapacity4.setText("Organics: " +design.getOrganicsCapacity() + "mu");
		widgets.add(lblCargoCapacity4);

		Label lblCargoCapacity5 = new Label(grpCargo,SWT.NONE);
		lblCargoCapacity5.setText("Liquids / Gas: " +design.getLiquidGasCapacity() + "mu");
		widgets.add(lblCargoCapacity5);

		Group grpAbilities = new Group(getDataGroup(),SWT.NONE);
		grpAbilities.setLayout(new FillLayout(SWT.VERTICAL));
		grpAbilities.setText("Special Abilities");
		data = new GridData();
		data.minimumWidth=250;
		data.verticalAlignment=SWT.TOP;
		grpAbilities.setLayoutData(data);
		widgets.add(grpAbilities);
		
		if(design.canMineAsteroid()) {
			Label lbl = new Label(grpAbilities,SWT.NONE);
			lbl.setText("Mine Asteroids");
			widgets.add(lbl);
		}
		
		if(design.canMineGasField()) {
			Label lbl = new Label(grpAbilities,SWT.NONE);
			lbl.setText("Mine Gas Field");
			widgets.add(lbl);
		}
		
		if(design.canMineGasField()) {
			Label lbl = new Label(grpAbilities,SWT.NONE);
			lbl.setText("Mine Gas Field");
			widgets.add(lbl);
		}
		
		if(design.canProbeField()) {
			Label lbl = new Label(grpAbilities,SWT.NONE);
			lbl.setText("Probe Asteroid / Gas Field");
			widgets.add(lbl);
		}
		
		if(design.canProbePlanet()) {
			Label lbl = new Label(grpAbilities,SWT.NONE);
			lbl.setText("Probe Planet");
			widgets.add(lbl);
		}
		
		if(design.hasBioLab()) {
			Label lbl = new Label(grpAbilities,SWT.NONE);
			lbl.setText("Prospect (Organics)");
			widgets.add(lbl);
		}
		
		if(design.hasPhysicsLab()) {
			Label lbl = new Label(grpAbilities,SWT.NONE);
			lbl.setText("Prospect (Mineral / Metal / Fissile)");
			widgets.add(lbl);
		}
		
		if(design.hasGeoLab()) {
			Label lbl = new Label(grpAbilities,SWT.NONE);
			lbl.setText("Prospect (Fuel / Gas / Metal / Liquid)");
			widgets.add(lbl);
		}

		Group grpHulls = new Group(getDataGroup(),SWT.NONE);
		grpHulls.setLayout(new GridLayout(1,false));
		grpHulls.setText("Hulls");
		data = new GridData();
		data.minimumWidth=500;
		data.verticalAlignment=SWT.TOP;
		data.horizontalSpan=2;
		grpHulls.setLayoutData(data);
		widgets.add(grpHulls);

		for(Items item : design.getHulls()) {
			Label lbl = new Label(grpHulls,SWT.NONE);
			lbl.setText(item.getQuantity() + " x " + item.getTypeClass().getName() + " [" + item.getType() + "]");
			widgets.add(lbl);
		}
		
}
}
