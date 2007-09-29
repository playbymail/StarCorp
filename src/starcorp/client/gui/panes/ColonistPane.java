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
import starcorp.common.entities.AColonists;
import starcorp.common.entities.Colony;
import starcorp.common.entities.Facility;
import starcorp.common.entities.Workers;

/**
 * starcorp.client.gui.panes.ColonistPane
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 28 Sep 2007
 */
public class ColonistPane extends AEntityPane {
	private final AColonists colonist;
	
	/**
	 * @param mainWindow
	 * @param entity
	 */
	public ColonistPane(MainWindow mainWindow, AColonists entity) {
		super(mainWindow, entity);
		this.colonist = entity;
	}
	
	@Override
	protected void createWidgets(List<Widget> widgets) {
		super.createWidgets(widgets);
		
		createLabel(getParent(), widgets, "Colony:");
		Colony colony = getTurnReport().getColony(colonist.getColony());
		createColonyLink(getParent(), widgets, colony, null);
		
		createLabel(getParent(),widgets, "Type:");
		createLabel(getParent(),widgets, colonist.getPopClass().getName());
		
		createLabel(getParent(),widgets,"Quantity:");
		createLabel(getParent(),widgets,format(colonist.getQuantity()));

		createLabel(getParent(),widgets,"Happiness:");
		createLabel(getParent(),widgets,format(colonist.getHappiness()) + "%");
		
		if(colonist instanceof Workers) {
			Workers w = (Workers) colonist;
			createLabel(getParent(), widgets, "Employer:");
			Facility f = getTurnReport().getFacility(w.getFacility());
			createFacilityLink(getParent(), widgets, f, null);
			
			createLabel(getParent(), widgets, "Salary:");
			createLabel(getParent(),widgets, "\u20a1 " + format(w.getSalary()));
		}
			
		
	}

}
