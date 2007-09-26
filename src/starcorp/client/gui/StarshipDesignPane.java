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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;

import starcorp.common.entities.Starship;
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
		
		Group grpHulls = new Group(getDataGroup(),SWT.NONE);
		grpHulls.setLayout(new GridLayout(1,false));
		grpHulls.setText("Hulls");
		GridData data = new GridData();
		data.horizontalSpan=2;
		data.verticalAlignment = SWT.TOP;
		data.horizontalAlignment=SWT.LEFT;
		data.minimumWidth=350;
		data.grabExcessHorizontalSpace = true;
		grpHulls.setLayoutData(data);
		widgets.add(grpHulls);

		for(Items item : design.getHulls()) {
			Label lbl = new Label(grpHulls,SWT.NONE);
			lbl.setText(item.getQuantity() + " x " + item.getTypeClass().getName() + " [" + item.getType() + "]");
			widgets.add(lbl);
		}
		
		Group grpShips = new Group(getDataGroup(),SWT.NONE);
		grpShips.setLayout(new GridLayout(1,false));
		grpShips.setText("Active Ships");
		data = new GridData();
		data.horizontalSpan=2;
		data.verticalAlignment = SWT.TOP;
		data.horizontalAlignment=SWT.LEFT;
		data.minimumWidth=350;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		grpShips.setLayoutData(data);
		widgets.add(grpShips);
		List<Starship> ships = mainWindow.getTurnReport().getPlayerStarships(design);
		if(ships != null && ships.size() > 0) {
			for(Starship ship : ships) {
				Label lbl = new Label(grpShips,SWT.NONE);
				lbl.setText(ship.getDisplayName());
				widgets.add(lbl);
			}
		}
		else {
			Label lbl = new Label(grpShips,SWT.NONE);
			lbl.setText("No active ships.");
			data = new GridData();
			data.horizontalSpan=2;
			lbl.setLayoutData(data);
			widgets.add(lbl);
		}
	}
}
