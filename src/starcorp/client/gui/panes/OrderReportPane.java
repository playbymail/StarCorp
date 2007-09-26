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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import starcorp.client.gui.ADataPane;
import starcorp.client.gui.windows.MainWindow;
import starcorp.common.turns.OrderReport;
import starcorp.common.turns.TurnOrder;

/**
 * starcorp.client.gui.OrderReportPane
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 25 Sep 2007
 */
public class OrderReportPane extends ADataPane {
	private TurnOrder order;
	private OrderReport report;
	
	public OrderReportPane(MainWindow mainWindow, TurnOrder order) {
		super(mainWindow);
		this.order = order;
		this.report = order.getReport();
	}

	@Override
	protected void createWidgets(List<Widget> widgets) {
		getParent().setText(order.getType().getName());
		
		Group grpArgs = createGroup(getParent(), widgets, "Order Arguments");
		grpArgs.setLayout(new RowLayout(SWT.VERTICAL));
		
		for(String arg : order.getArgs()) {
			createLabel(grpArgs, widgets, arg);
		}
		
		Text txtReport = createMultilineTextInput(getParent(), widgets, "Report:");
		txtReport.setText(report.getDescription() == null ? "" : report.getDescription());
		
	}
}
