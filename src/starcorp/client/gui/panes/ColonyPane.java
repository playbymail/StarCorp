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
import starcorp.common.entities.Colony;

/**
 * starcorp.client.gui.ColonyPane
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 25 Sep 2007
 */
public class ColonyPane extends AEntityPane {
	private Colony colony;
	
	/**
	 * @param entity
	 */
	public ColonyPane(MainWindow mainWindow, Colony entity) {
		super(mainWindow, entity);
		this.colony = entity;
	}

	@Override
	protected void createWidgets(List<Widget> widgets) {
		super.createWidgets(widgets);
		
		createLabel(getParent(), widgets, "Government:");
		createLabel(getParent(), widgets, colony.getGovernment().getDisplayName());
		
		createLabel(getParent(), widgets, "Hazard Level:");
		createLabel(getParent(), widgets, String.valueOf(colony.getHazardLevel()));
		
		createLabel(getParent(), widgets, "Founded:");
		createLabel(getParent(), widgets, colony.getFoundedDate().toString());

	}

}
