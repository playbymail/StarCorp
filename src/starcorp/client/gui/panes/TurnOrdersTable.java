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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import starcorp.client.gui.ADataEntryWindow;
import starcorp.client.gui.ATablePane;
import starcorp.client.gui.windows.TurnOrderWindow;
import starcorp.common.turns.Turn;
import starcorp.common.turns.TurnOrder;
import starcorp.common.types.OrderType;

/**
 * starcorp.client.gui.TurnOrdersTable
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 25 Sep 2007
 */
public class TurnOrdersTable extends ATablePane {
	private final Turn turn;
	
	public TurnOrdersTable(Turn turn, ADataEntryWindow mainWindow) {
		super(mainWindow);
		this.turn = turn;
	}
	
	@Override
	protected String getTableName() {
		return "Turn Orders";
	}

	@Override
	protected int countColumns() {
		return 7;
	}

	@Override
	protected String getColumnName(int index) {
		switch(index) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			return "";
		}
		return null;
	}

	@Override
	protected void populate() {
		for(TurnOrder order : turn.getOrders()) {
			if(order.getType() == null)
				continue;
			String[] values = new String[countColumns()];
			values[0] = order.getType().getKey();
			for(int i = 1; i < countColumns(); i++) {
				values[i] = order.get((i-1));
			}
			createRow(values);
		}
	}

	@Override
	protected void columnEdited(int row, int column, String value) {
		TurnOrder order = turn.getOrders().get(row);
		if(column == 0) {
			order.setType(OrderType.getType(value));
		}
		else {
			order.set((column-1), value);
		}
		TurnOrderWindow orderWindow = (TurnOrderWindow)super.window;
		orderWindow.turnEdited();
	}

	@Override
	protected void createWidgets(List<Widget> widgets) {
		super.createWidgets(widgets);
		Group grp = createGroup(getParent(), widgets, "");
		RowLayout layout = new RowLayout();
		layout.marginWidth=10;
		layout.marginHeight=5;
		layout.justify=true;
		layout.fill=true;
		layout.spacing=480;
		grp.setLayout(layout);
		createButton(grp, widgets, "Clear")
		.addListener(SWT.Selection, new Listener() {
			public void handleEvent (Event event) {
				((TurnOrderWindow)window).clearTurn();
			}
		});
		createButton(grp, widgets, "Submit")
		.addListener(SWT.Selection, new Listener() {
			public void handleEvent (Event event) {
				window.getMainWindow().submitTurn();
			}
		});
	}

}
