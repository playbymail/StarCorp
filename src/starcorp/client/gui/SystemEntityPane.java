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

import org.eclipse.swt.widgets.Widget;

import starcorp.common.entities.StarSystemEntity;

/**
 * starcorp.client.gui.SystemEntityPane
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 26 Sep 2007
 */
public class SystemEntityPane extends AEntityPane {

	public SystemEntityPane(MainWindow mainWindow,StarSystemEntity entity) {
		super(mainWindow,entity);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see starcorp.client.gui.ADataPane#createWidgets(java.util.List)
	 */
	@Override
	protected void createWidgets(List<Widget> widgets) {
		// TODO Auto-generated method stub

	}

}
