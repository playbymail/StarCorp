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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Widget;

import starcorp.client.gui.AEntityPane;
import starcorp.client.gui.widgets.Hyperlink;
import starcorp.client.gui.windows.MainWindow;
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
		
		createLabel(getParent(), widgets, "Design:");
		
		createDesignLink(getParent(), widgets, ship.getDesign(), null);
		
		createLabel(getParent(), widgets, "Location:");
		
		String locDesc = ship.getLocationDescription();
		
		if(ship.getColony() != null) {
			createColonyLink(getParent(), widgets, ship.getColony(), locDesc);
		}
		else if(ship.getPlanet() != null) {
			createPlanetLink(getParent(), widgets, ship.getPlanet(), locDesc);
		}
		else {
			createLabel(getParent(), widgets, locDesc);
		}
		GridData data = new GridData();
		data.horizontalSpan=2;
		data.grabExcessHorizontalSpace=true;
		data.grabExcessVerticalSpace=true;
		Group grpCargo = createGroup(getParent(), widgets, "Cargo");
		grpCargo.setLayout(new RowLayout(SWT.VERTICAL));
		grpCargo.setLayoutData(data);
		
		for(Items item : ship.getCargo()) {
			createLabel(grpCargo, widgets, item.toString());
		}
		if(ship.getCargo().size() < 1) {
			createLabel(grpCargo, widgets, "None");
		}
		
		// TODO display nearby star systems, planets or other system entities
		// TODO add buttons to add orders suitable to current starship
	}
}
