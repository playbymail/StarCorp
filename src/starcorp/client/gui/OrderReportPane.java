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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

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
		getDataGroup().setText(order.getType().getName());
		
		Label lblArguments = new Label(getDataGroup(),SWT.NONE);
		lblArguments.setText("Arguments:");
		GridData data = new GridData();
		data.verticalSpan = order.getArgs().size();
		lblArguments.setLayoutData(data);
		widgets.add(lblArguments);
		
		for(String arg : order.getArgs()) {
			Text txt = new Text(getDataGroup(),SWT.NONE);
			txt.setEditable(false);
			txt.setText(arg);
			widgets.add(txt);
		}
		
		Text txtReport = new Text(getDataGroup(),SWT.BORDER | SWT.MULTI);
		txtReport.setText(report.getDescription() == null ? "" : report.getDescription());
		data =new GridData();
		data.horizontalSpan=2;
		txtReport.setLayoutData(data);
		widgets.add(txtReport);
		
	}
}
