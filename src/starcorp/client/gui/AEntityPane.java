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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Widget;

import starcorp.common.entities.ANamedEntity;
import starcorp.common.entities.IEntity;

/**
 * starcorp.client.gui.AEntityPane
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 25 Sep 2007
 */
public abstract class AEntityPane extends ADataPane {

	private IEntity entity;
	
	public AEntityPane(MainWindow mainWindow, IEntity entity) {
		super(mainWindow);
		this.entity = entity;
	}

	@Override
	protected void createWidgets(List<Widget> widgets) {
		System.out.println("AEntityPane createWidgets: " + entity);
		getDataGroup().setText(entity.getDisplayName());
		
	}
}
