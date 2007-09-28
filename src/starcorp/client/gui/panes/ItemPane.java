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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Widget;

import starcorp.client.gui.ATypePane;
import starcorp.client.gui.windows.MainWindow;
import starcorp.common.types.AFactoryItem;
import starcorp.common.types.AItemType;
import starcorp.common.types.BuildingModules;
import starcorp.common.types.ConsumerGoods;
import starcorp.common.types.IndustrialGoods;
import starcorp.common.types.Items;
import starcorp.common.types.Resources;
import starcorp.common.types.StarshipHulls;

/**
 * starcorp.client.gui.panes.ItemPane
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 26 Sep 2007
 */
public class ItemPane extends ATypePane {

	private AItemType type;
	
	/**
	 * @param mainWindow
	 * @param type
	 */
	public ItemPane(MainWindow mainWindow, AItemType type) {
		super(mainWindow, type);
		this.type = type;
		if(type == null) {
			throw new NullPointerException("Invalid type");
		}
	}

	@Override
	protected void createWidgets(List<Widget> widgets) {
		super.createWidgets(widgets);
		
		createLabel(getParent(), widgets, "Key:");
		createLabel(getParent(), widgets, type.getKey());
		
		createLabel(getParent(), widgets, "Mass:");
		createLabel(getParent(), widgets, format(type.getMassUnits()));
		
		if(type instanceof ConsumerGoods) {
			createLabel(getParent(), widgets, "Consumer Goods:");
		}
		
		if(type instanceof IndustrialGoods) {
			createLabel(getParent(), widgets, "Industrial Goods:");
		}
		
		if(type instanceof Resources) {
			createLabel(getParent(), widgets, "Resources:");
		}
		
		if(type instanceof StarshipHulls) {
			createLabel(getParent(), widgets, "Starship Hulls:");
		}
		
		if(type instanceof BuildingModules) {
			createLabel(getParent(), widgets, "Building Modules:");
		}
		
		createLabel(getParent(), widgets, type.getSubCategory());

		if(type instanceof AFactoryItem) {
			AFactoryItem f = (AFactoryItem) type;
			Set<Items> components = f.getComponent();
			Group grp = createGroup(getParent(), widgets, "Components");
			grp.setLayout(new RowLayout(SWT.VERTICAL));
			GridData data = new GridData();
			data.horizontalSpan=2;
			grp.setLayoutData(data);
			for(Items item : components) {
				createItemLink(grp, widgets, item, null);
			}
		}
		
		
	}

}
