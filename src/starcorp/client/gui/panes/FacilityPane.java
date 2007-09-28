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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import starcorp.client.gui.AEntityPane;
import starcorp.client.gui.windows.MainWindow;
import starcorp.common.entities.AColonists;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.Facility;
import starcorp.common.entities.FactoryQueueItem;
import starcorp.common.turns.TurnOrder;
import starcorp.common.types.AItemType;
import starcorp.common.types.Factory;

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

		createLabel(getParent(), widgets, "Owner:");
		createCorporationLink(getParent(), widgets, facility.getOwner(), null);

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
			createLabel(getParent(), widgets, "\u20a1 " + format(facility.getServiceCharge()));
		}
		
		if(facility.getTypeClass() instanceof Factory) {
			Group grp = createGroup(getParent(), widgets, "Factory Orders");
			GridData data = new GridData();
			data.horizontalSpan=2;
			grp.setLayoutData(data);
			grp.setLayout(new GridLayout(5,false));
			List<AItemType> types = ((Factory)facility.getTypeClass()).canBuild();
			final Combo c = createTypeSelection(grp, widgets, types, "Item:");
			final Text txt = createIntegerInput(grp, widgets, "Quantity:");
			Button add = createButton(grp, widgets, "Add");
			add.addListener(SWT.Selection, new Listener() {
				public void handleEvent (Event event) {
					AItemType type = (AItemType) getComboValue(c);
					int qty = getIntegerTextValue(txt);
					TurnOrder order = buildOrder(facility, type, qty);
					mainWindow.addTurnOrder(order);
				}
			});
		}
		
		Corporation player = getTurnReport().getTurn().getCorporation();
		if(player.equals(facility.getOwner())) {
			Group grp = createGroup(getParent(), widgets, "Employees");
			GridData data = new GridData();
			data.minimumWidth=100;
			data.minimumHeight=100;
			data.verticalAlignment=SWT.TOP;
			data.grabExcessHorizontalSpace=true;
			data.grabExcessVerticalSpace=true;
			if(!(facility.getTypeClass() instanceof Factory)) {
				data.horizontalSpan=2;
			}
			grp.setLayoutData(data);
			grp.setLayout(new GridLayout(1,false));
			
			Set<AColonists> employees = getTurnReport().getEmployees(facility);
			double efficiency = facility.getEfficiency(employees);

			for(AColonists colonist : employees) {
				createColonistLink(grp, widgets, colonist, null);
			}
			
			createLabel(grp, widgets, "Efficiency: " + formatPercentage(efficiency) + "%");

			if(facility.getTypeClass() instanceof Factory) {
				data = new GridData();
				data.verticalAlignment=SWT.TOP;
				data.minimumWidth=100;
				data.minimumHeight=300;
				data.grabExcessHorizontalSpace=true;
				data.grabExcessVerticalSpace=true;
				grp = createGroup(getParent(), widgets, "Factory Queue");
				grp.setLayoutData(data);
				grp.setLayout(new GridLayout(2,false));
				
				List<FactoryQueueItem> list = getTurnReport().getQueue(facility);
				for(int i = (list.size() - 1); i > -1; i--) {
					FactoryQueueItem item = list.get(i);
					createLabel(grp, widgets, item.getPosition() + ":");
					createItemLink(grp, widgets, item.getItem(), null);
				}
			}
		}
		
	}
}
