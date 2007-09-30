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

import starcorp.client.gui.ADataEntryWindow;
import starcorp.client.gui.ATablePane;
import starcorp.client.gui.windows.SearchItemsWindow;
import starcorp.common.entities.Colony;
import starcorp.common.entities.ColonyItem;
import starcorp.common.types.Items;

/**
 * starcorp.client.gui.ItemsTable
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 25 Sep 2007
 */
public class ItemsTable extends ATablePane {
	private final SearchItemsWindow searchWindow;
	
	public ItemsTable(ADataEntryWindow mainWindow) {
		super(mainWindow);
		this.searchWindow = (SearchItemsWindow) mainWindow;
	}

	@Override
	protected int countColumns() {
		return 3;
	}

	@Override
	protected String getColumnName(int index) {
		switch(index) {
		case 0 : return "Type";
		case 1 : return "Quantity";
		case 2 : return "Colony";
		}
		return "";
	}

	@Override
	protected boolean isEditable() {
		return false;
	}

	@Override
	public void populate() {
		int total = searchWindow.countFilteredItems();
		if(total < 1) {
			String[] values = {"No items found"};
			createRow(values);
		}
		else {
			int page = searchWindow.getPage();
			int start = (page - 1) * SearchItemsWindow.ITEMS_PER_PAGE;
			int end = start + SearchItemsWindow.ITEMS_PER_PAGE;
			System.out.println("ItemsTable populate: " + total + " total " + page + " page " + start + " start " + end + " end");
			if(start < 0) start = 0;
			if(end > total) end = total;
			for(int n = start; n < end; n++) {
				ColonyItem item = searchWindow.get(n);
				if(item == null)
					return;
//				System.out.println("ItemsTable " + item);
				Items items = item.getItem();
				Colony colony = searchWindow.getReport().getColony(item.getColony()); 
				String[] values = new String[countColumns()];
				values[0] = items.getTypeClass().getName();
				values[1] = format(items.getQuantity());
				values[2] = colony.getDisplayName();
				createRow(values);
			}
		}
	}

	@Override
	protected int getColumnWidth(int index) {
		switch(index) {
		case 0 : return 120;
		case 1 : return 80;
		case 2 : return 200;
		}
		return super.getColumnWidth(index);
	}

}
