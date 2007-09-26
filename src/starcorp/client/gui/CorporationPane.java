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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;

import starcorp.common.entities.Corporation;

/**
 * starcorp.client.gui.CorporationPane
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 25 Sep 2007
 */
public class CorporationPane extends AEntityPane {

	private Corporation corp;
	
	public CorporationPane(MainWindow mainWindow, Corporation corp) {
		super(mainWindow, corp);
		this.corp = corp;
	}

	@Override
	protected void createWidgets(List<Widget> widgets) {
		super.createWidgets(widgets);
		Label lblCredits = new Label(getDataGroup(),SWT.NONE);
		lblCredits.setText("Credits:");
		widgets.add(lblCredits);
		
		Label lblCreditAmount = new Label(getDataGroup(),SWT.NONE);
		lblCreditAmount.setText("0"); // TODO get credit account
		widgets.add(lblCreditAmount);
		
		Label lblShips = new Label(getDataGroup(),SWT.NONE);
		lblShips.setText("Ships:");
		widgets.add(lblShips);
		
		Label lblShipQuantity = new Label(getDataGroup(),SWT.NONE);
		lblShipQuantity.setText(String.valueOf(mainWindow.getTurnReport().countPlayerStarships()));
		widgets.add(lblShipQuantity);
		
		Label lblFacilities = new Label(getDataGroup(),SWT.NONE);
		lblFacilities.setText("Facilities:");
		widgets.add(lblFacilities);
		
		Label lblFacilityQuantity = new Label(getDataGroup(),SWT.NONE);
		lblFacilityQuantity.setText(String.valueOf(mainWindow.getTurnReport().countPlayerFacilities()));
		widgets.add(lblFacilityQuantity);
		
		// TODO add cash transactions
	}
}
