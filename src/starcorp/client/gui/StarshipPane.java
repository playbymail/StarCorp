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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;

import starcorp.common.entities.Starship;
import starcorp.common.types.Items;

/**
 * starcorp.client.gui.StarshipPane
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 25 Sep 2007
 */
public class StarshipPane extends AEntityPane {
	private Starship ship;
	
	public StarshipPane (MainWindow mainWindow, Starship ship) {
		super(mainWindow, ship);
		this.ship = ship;
	}

	@Override
	protected void createWidgets(List<Widget> widgets) {
		super.createWidgets(widgets);
		
		Label lblDesign = new Label(getDataGroup(),SWT.NONE);
		lblDesign.setText("Design:");
		widgets.add(lblDesign);
		
		Label lblDesignName = new Label(getDataGroup(),SWT.NONE);
		lblDesignName.setText(ship.getDesign().getName() +" [" + ship.getDesign().getID() +"]");
		widgets.add(lblDesignName);
		
		Label lblLocation = new Label(getDataGroup(),SWT.NONE);
		lblLocation.setText("Location:");
		widgets.add(lblLocation);
		
		Label lblLocationDesc = new Label(getDataGroup(),SWT.NONE);
		lblLocationDesc.setText(ship.getLocationDescription());
		widgets.add(lblLocationDesc);
		
		Label lblCargo = new Label(getDataGroup(),SWT.NONE);
		lblCargo.setText("Cargo:");
		GridData data = new GridData();
		data.verticalSpan=ship.getCargo().size();
		lblCargo.setLayoutData(data);
		widgets.add(lblCargo);
		
		for(Items item : ship.getCargo()) {
			Label lbl = new Label(getDataGroup(),SWT.NONE);
			lbl.setText(item.toString());
			widgets.add(lbl);
		}
		
		// TODO display nearby star systems, planets or other system entities
	}
}
