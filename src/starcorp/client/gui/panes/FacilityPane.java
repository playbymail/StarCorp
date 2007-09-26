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

import org.eclipse.swt.widgets.Widget;

import starcorp.client.gui.AEntityPane;
import starcorp.client.gui.windows.MainWindow;
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
		
		createLabel(getParent(), widgets, "Colony:");
		createColonyLink(getParent(), widgets, facility.getColony(), null);

		createLabel(getParent(), widgets, "Type:");
		createFacilityTypeLink(getParent(), widgets, facility.getTypeClass(), null);

		createLabel(getParent(), widgets, "Open:");
		createLabel(getParent(), widgets, facility.isOpen() ? "yes" : "no");

		createLabel(getParent(), widgets, "Powered:");
		createLabel(getParent(), widgets, facility.isPowered() ? "yes" : "no");

		createLabel(getParent(), widgets, "Built:");
		createLabel(getParent(), widgets, facility.getBuiltDate().toString());

		if(facility.getServiceCharge() > 0) {
			createLabel(getParent(), widgets, "Service Charge:");
			createLabel(getParent(), widgets, "\u20a1 " + facility.getServiceCharge());
		}
		
		// TODO add workers, efficiency and queue items
	}
}
