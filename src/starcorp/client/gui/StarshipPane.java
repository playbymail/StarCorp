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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
		
		Hyperlink lnkDesign = new Hyperlink(getDataGroup(),SWT.NONE);
		lnkDesign.setText(ship.getDesign().getDisplayName());
		lnkDesign.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				mainWindow.setDataPane(new StarshipDesignPane(mainWindow,ship.getDesign()));
			}
			public void widgetSelected(SelectionEvent e) {
				mainWindow.setDataPane(new StarshipDesignPane(mainWindow,ship.getDesign()));
			}
		});
		widgets.add(lnkDesign);
		
		Label lblLocation = new Label(getDataGroup(),SWT.NONE);
		lblLocation.setText("Location:");
		widgets.add(lblLocation);
		
		String locDesc = ship.getLocationDescription();
		
		if(ship.getColony() != null) {
			Hyperlink lnk = new Hyperlink(getDataGroup(),SWT.NONE);
			lnk.setText(locDesc);
			lnk.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
					mainWindow.setDataPane(new ColonyPane(mainWindow,ship.getColony()));
				}
				public void widgetSelected(SelectionEvent e) {
					mainWindow.setDataPane(new ColonyPane(mainWindow,ship.getColony()));
				}
			});
		}
		else if(ship.getPlanet() != null) {
			Hyperlink lnk = new Hyperlink(getDataGroup(),SWT.NONE);
			lnk.setText(locDesc);
			lnk.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
					mainWindow.setDataPane(new SystemEntityPane(mainWindow,ship.getPlanet()));
				}
				public void widgetSelected(SelectionEvent e) {
					mainWindow.setDataPane(new SystemEntityPane(mainWindow,ship.getPlanet()));
				}
			});
		}
		else {
			Label lbl = new Label(getDataGroup(),SWT.NONE);
			lbl.setText(locDesc);
			widgets.add(lbl);
		}
		
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
		if(ship.getCargo().size() < 1) {
			Label lbl = new Label(getDataGroup(),SWT.NONE);
			lbl.setText("None");
			widgets.add(lbl);
		}
		
		// TODO display nearby star systems, planets or other system entities
		// TODO add buttons to add orders suitable to current starship
	}
}
