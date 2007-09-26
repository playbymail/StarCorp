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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * starcorp.client.gui.Toolbar
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 25 Sep 2007
 */
public class Toolbar implements IComponent  {

	private final MainWindow mainWindow;
	
	private ToolBar toolbar;
	private List<ToolItem> items = new ArrayList<ToolItem>();
	
	private ToolItem saveTurn;
	private ToolItem submitTurn;
	private ToolItem searchItems;
	private ToolItem searchMarket;
	private ToolItem searchLaws;
	
	public Toolbar(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
	
	public void dispose() {
		for(ToolItem item : items) {
			item.dispose();
		}
		toolbar.dispose();
	}

	public void open(Composite parent) {
		toolbar = new ToolBar(parent,SWT.FLAT | SWT.WRAP);
		
		// TODO add images to tool items
		
		ToolItem loadTurnReport = new ToolItem(toolbar, SWT.PUSH);
		loadTurnReport.setText("Load Report");
		loadTurnReport.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				mainWindow.openLoadReport();
			}
		});
		items.add(loadTurnReport);
		
		items.add(new ToolItem(toolbar, SWT.SEPARATOR));
		
		ToolItem newTurn = new ToolItem(toolbar, SWT.PUSH);
		newTurn.setText("New Turn");
		newTurn.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				mainWindow.setCurrentTurn(null);
				mainWindow.openTurnWindow();			
			}
		});
		items.add(newTurn);
		
		ToolItem loadTurn = new ToolItem(toolbar, SWT.PUSH);
		loadTurn.setText("Load Turn");
		loadTurn.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				mainWindow.openLoadTurn();	
			}
		});
		items.add(loadTurn);
		
		saveTurn = new ToolItem(toolbar, SWT.PUSH);
		saveTurn.setText("Save current turn");
		saveTurn.setEnabled(false);
		saveTurn.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				mainWindow.openSaveTurn();
			}
		});
		items.add(saveTurn);
		
		submitTurn = new ToolItem(toolbar, SWT.PUSH);
		submitTurn.setText("Submit Turn");
		submitTurn.setEnabled(false);
		submitTurn.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				mainWindow.submitTurn();			}
		});
		items.add(submitTurn);
		
		items.add(new ToolItem(toolbar, SWT.SEPARATOR));
		
		searchItems = new ToolItem(toolbar, SWT.PUSH);
		searchItems.setText("Search Items");
		searchItems.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				mainWindow.openSearchItemsWindow();
			}
		});
		items.add(searchItems);
		
		searchMarket= new ToolItem(toolbar, SWT.PUSH);
		searchMarket.setText("Search Market");
		searchMarket.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				mainWindow.openSearchMarketWindow();
			}
		});
		items.add(searchMarket);
		
		searchLaws = new ToolItem(toolbar, SWT.PUSH);
		searchLaws.setText("Search Laws");
		searchLaws.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				mainWindow.openSearchLawWindow();
			}
		});
		items.add(searchLaws);
		
		searchItems.setEnabled(false);
		searchMarket.setEnabled(false);
		searchLaws.setEnabled(false);
		
		items.add(new ToolItem(toolbar, SWT.SEPARATOR));
		
		ToolItem designShip = new ToolItem(toolbar, SWT.PUSH);
		designShip.setText("Design Ship");
		designShip.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				mainWindow.openStarshipDesignWindow();
			}
		});
		items.add(designShip);
		
}
	
	public void setHasReport(boolean yes) {
		searchItems.setEnabled(yes);
		searchMarket.setEnabled(yes);
		searchLaws.setEnabled(yes);
	}
	public void setEnableSave(boolean yes) {
		saveTurn.setEnabled(yes);
	}
	
	public void setEnableSubmit(boolean yes) {
		submitTurn.setEnabled(yes);
	}

	public void pack() {
		toolbar.pack();
	}

	public Point computeSize() {
		return toolbar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}

}
