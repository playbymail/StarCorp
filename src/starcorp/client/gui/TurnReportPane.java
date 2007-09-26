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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import starcorp.common.turns.Turn;
import starcorp.common.turns.TurnError;
import starcorp.common.turns.TurnReport;
import starcorp.common.types.GalacticDate;

/**
 * starcorp.client.gui.TurnReportPane
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 25 Sep 2007
 */
public class TurnReportPane extends ADataPane {

	private TurnReport report;
	
	public TurnReportPane(MainWindow mainWindow, TurnReport report) {
		super(mainWindow);
		this.report = report;
	}
	
	/* (non-Javadoc)
	 * @see starcorp.client.gui.ADataPane#createWidgets(java.util.List)
	 */
	@Override
	protected void createWidgets(List<Widget> widgets) {
		getDataGroup().setText("Turn Report Summary");
		
		Turn turn = report.getTurn();
		if(turn == null) {
			Label lbl = new Label(getDataGroup(),SWT.NONE);
			lbl.setText("No turn data.");
			GridData data = new GridData();
			data.horizontalSpan=2;
			lbl.setLayoutData(data);
			widgets.add(lbl);
		}
		else {
			GalacticDate turnDate = turn.getProcessedDate();
			int orderCount = turn.getOrders().size();
			int errorCount = turn.getErrors().size();
			
			Label lblDate = new Label(getDataGroup(),SWT.NONE);
			lblDate.setText("Galactic Date:");
			widgets.add(lblDate);
			if(turnDate != null) {
				Text txtDate = new Text(getDataGroup(),SWT.NONE);
				txtDate.setText(turn.getProcessedDate().toString());
				txtDate.setEditable(false);
				widgets.add(txtDate);
			}
			else {
				Text txtDate = new Text(getDataGroup(),SWT.NONE);
				txtDate.setText("N/A");
				txtDate.setEditable(false);
				widgets.add(txtDate);
			}
			
			Label lblOrders = new Label(getDataGroup(),SWT.NONE);
			lblOrders.setText("Orders:");
			widgets.add(lblOrders);

			Label lblOrderCount = new Label(getDataGroup(),SWT.NONE);
			lblOrderCount.setText(String.valueOf(orderCount));
			widgets.add(lblOrderCount);

			Label lblError = new Label(getDataGroup(),SWT.NONE);
			lblError.setText("Errors:");
			widgets.add(lblError);
			
			Label lblErrorCount = new Label(getDataGroup(),SWT.NONE);
			lblErrorCount.setText(String.valueOf(errorCount));
			widgets.add(lblErrorCount);

			if(errorCount > 0) {
				Group grpErrors = new Group(getDataGroup(), SWT.BORDER);
				grpErrors.setText("Errors:");
				grpErrors.setLayout(new GridLayout(1,true));
				GridData data = new GridData();
				data.horizontalSpan=2;
				grpErrors.setLayoutData(data);
				widgets.add(grpErrors);
				
				for(TurnError error :  turn.getErrors()) {
					Label lbl = new Label(grpErrors,SWT.NONE);
					lbl.setText(error.getMessage());
					widgets.add(lbl);
				}
			}
		}
	}

}
