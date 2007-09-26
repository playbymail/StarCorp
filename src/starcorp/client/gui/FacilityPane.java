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

import starcorp.common.entities.Facility;

/**
 * starcorp.client.gui.FacilityPane
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 25 Sep 2007
 */
public class FacilityPane extends AEntityPane {

	private Facility facility;
	
	public FacilityPane(MainWindow mainWindow, Facility facility) {
		super(mainWindow, facility);
		this.facility = facility;
	}

	@Override
	protected void createWidgets(List<Widget> widgets) {
		super.createWidgets(widgets);
		
		Label lblType = new Label(getDataGroup(),SWT.NONE);
		lblType.setText("Type:");
		widgets.add(lblType);
		
		Label lblFacilityType = new Label(getDataGroup(),SWT.NONE);
		lblFacilityType.setText(facility.getTypeClass().getName());
		widgets.add(lblFacilityType);
		
		Label lblOpen = new Label(getDataGroup(),SWT.NONE);
		lblOpen.setText("Open:");
		widgets.add(lblOpen);
		
		Label lblOpenValue = new Label(getDataGroup(),SWT.NONE);
		lblOpenValue.setText(facility.isOpen() ? "yes" : "no");
		widgets.add(lblOpenValue);

		Label lblPowered = new Label(getDataGroup(),SWT.NONE);
		lblPowered.setText("Powered:");
		widgets.add(lblPowered);
		
		Label lblPoweredValue = new Label(getDataGroup(),SWT.NONE);
		lblPoweredValue.setText(facility.isPowered() ? "yes" : "no");
		widgets.add(lblPoweredValue);
		
		Label lblFounded = new Label(getDataGroup(),SWT.NONE);
		lblFounded.setText("Built:");
		widgets.add(lblFounded);
		
		Label lblFoundedDate = new Label(getDataGroup(),SWT.NONE);
		lblFoundedDate.setText(facility.getBuiltDate().toString());
		widgets.add(lblFoundedDate);

		if(facility.getServiceCharge() > 0) {
			Label lblCharge = new Label(getDataGroup(),SWT.NONE);
			lblCharge.setText("Service Charge:");
			widgets.add(lblCharge);
			
			Label lblChargeDesc = new Label(getDataGroup(),SWT.NONE);
			lblChargeDesc.setText("\u20a1 " + facility.getServiceCharge());
			widgets.add(lblChargeDesc);
		}
		
		// TODO add workers and queue items
	}
}
