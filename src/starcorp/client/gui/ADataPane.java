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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Widget;

/**
 * starcorp.client.gui.ADataPane
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 25 Sep 2007
 */
public abstract class ADataPane implements IComponent {

	protected final MainWindow mainWindow;
	private List<Widget> widgets = new ArrayList<Widget>();
	private Group dataGroup;
	
	public ADataPane(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
	
	public void dispose() {
		dataGroup.dispose();
		for(Widget w : widgets) {
			w.dispose();
		}
		
	}

	public void open(Composite parent) {
		dataGroup = new Group(parent,SWT.NONE);
		dataGroup.setLayout(new GridLayout(2,false));
		GridData data = new GridData();
		data.verticalAlignment = SWT.TOP;
		data.horizontalAlignment=SWT.LEFT;
		data.minimumWidth=400;
		data.minimumHeight=500;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		dataGroup.setLayoutData(data);
		createWidgets(widgets);
		dataGroup.setSize(400, 500);
		
	}

	public void pack() {
		dataGroup.redraw();
	}
	
	protected Group getDataGroup() {
		return dataGroup;
	}
	
	protected abstract void createWidgets(List<Widget> widgets);

	public Point computeSize() {
		return dataGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}

}
