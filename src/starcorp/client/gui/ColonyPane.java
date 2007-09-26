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

import starcorp.common.entities.Colony;
import starcorp.common.entities.IEntity;

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
		
		Label lblGovernment = new Label(getDataGroup(),SWT.NONE);
		lblGovernment.setText("Government:");
		widgets.add(lblGovernment);
		
		Label lblGovernmentName = new Label(getDataGroup(),SWT.NONE);
		lblGovernmentName.setText(colony.getGovernment().getDisplayName());
		widgets.add(lblGovernmentName);
		
		Label lblHazard = new Label(getDataGroup(),SWT.NONE);
		lblHazard.setText("Hazard Level:");
		widgets.add(lblHazard);
		
		Label lblHazardLevel = new Label(getDataGroup(),SWT.NONE);
		lblHazardLevel.setText(String.valueOf(colony.getHazardLevel()));
		widgets.add(lblHazardLevel);
		
		Label lblFounded = new Label(getDataGroup(),SWT.NONE);
		lblFounded.setText("Founded:");
		widgets.add(lblFounded);
		
		Label lblFoundedDate = new Label(getDataGroup(),SWT.NONE);
		lblFoundedDate.setText(colony.getFoundedDate().toString());
		widgets.add(lblFoundedDate);
	}

}
