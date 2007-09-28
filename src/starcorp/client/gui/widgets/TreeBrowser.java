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
package starcorp.client.gui.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import starcorp.client.gui.IComponent;
import starcorp.client.gui.panes.ColonyPane;
import starcorp.client.gui.panes.CorporationPane;
import starcorp.client.gui.panes.FacilityPane;
import starcorp.client.gui.panes.FacilityTypePane;
import starcorp.client.gui.panes.OrderReportPane;
import starcorp.client.gui.panes.StarshipDesignPane;
import starcorp.client.gui.panes.StarshipPane;
import starcorp.client.gui.panes.TurnReportPane;
import starcorp.client.gui.windows.MainWindow;
import starcorp.common.entities.Colony;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.Facility;
import starcorp.common.entities.Starship;
import starcorp.common.entities.StarshipDesign;
import starcorp.common.turns.Turn;
import starcorp.common.turns.TurnOrder;
import starcorp.common.turns.TurnReport;
import starcorp.common.types.AFacilityType;

/**
 * starcorp.client.gui.TreeBrowser
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 25 Sep 2007
 */
public class TreeBrowser implements IComponent  {
	// TODO add context menus
	private final MainWindow mainWindow;
	
	private Composite parent;
	private Tree tree;
	private List<TreeItem> listItemOrders = new ArrayList<TreeItem>();
	private List<TreeItem> listItemShips = new ArrayList<TreeItem>();
	private List<TreeItem> listItemDesigns = new ArrayList<TreeItem>();
	private List<TreeItem> listItemFacilities = new ArrayList<TreeItem>();
	
	private TurnReport report;
	
	public TreeBrowser(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
	
	public void setReport(TurnReport report) {
		this.report = report;
		dispose(listItemDesigns);
		dispose(listItemOrders);
		dispose(listItemShips);
		dispose(listItemFacilities);
		if(tree != null && !tree.isDisposed())
			tree.dispose();
		tree = new Tree(parent, SWT.BORDER);
		tree.setSize(300,500);
		
		GridData data = new GridData();
		data.verticalAlignment = SWT.TOP;
		data.horizontalAlignment=SWT.LEFT;
		data.minimumWidth=200;
		data.minimumHeight=530;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		tree.setLayoutData(data);

		tree.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				mainWindow.redraw();
			}
		});

		TreeItem itemCorporation = new TreeItem(tree,SWT.NONE);
		itemCorporation.setText(getCorporationName());
		if(report != null) {
			TreeItem itemReport = new TreeItem(tree,SWT.NONE);
			itemReport.setText("Turn Report");

			Turn turn = report.getTurn();
			Corporation c = turn.getCorporation();
			itemCorporation.setText(c.getName() + " [" + c.getID() + "]");
			itemCorporation.setData(c);
			itemReport.setData(report);
			tree.addListener(SWT.Selection, listenCorporation(turn.getCorporation()));
			tree.addListener(SWT.Selection, listenReport(report));
			for(TurnOrder order : turn.getOrders()) {
				TreeItem item = new TreeItem(itemReport,SWT.NONE);
				tree.addListener(SWT.Selection, listenOrder(order));
				item.setText(order.getType() == null ? "Unknown" : order.getType().getName());
				item.setData(order);
				listItemOrders.add(item);
				// TODO context menu: redo order
			}
			Set<StarshipDesign> designs = report.getPlayerDesigns();
			if(designs.size() > 0) {
				TreeItem itemDesigns = new TreeItem(tree,SWT.NONE);
				itemDesigns.setText("Designs");
				for(StarshipDesign design : designs) {
					TreeItem shipItem = new TreeItem(itemDesigns,SWT.NONE);
					tree.addListener(SWT.Selection, listenDesign(design));
					shipItem.setText(design.getName() +" [" + design.getID() +"]");
					shipItem.setData(design);
					listItemDesigns.add(shipItem);
					// TODO context menu: build ship
					Set<Starship> ships = mainWindow.getTurnReport().getPlayerStarships(design);
					if(ships != null && ships.size() > 0) {
						for(Starship ship : ships) {
							TreeItem item = new TreeItem(shipItem,SWT.NONE);
							tree.addListener(SWT.Selection, listenShip(ship));
							item.setText(ship.getName() +" [" + ship.getID() +"]");
							item.setData(ship);
							listItemDesigns.add(item);
							// TODO context menu with suitable orders for ships
						}
					}
				}
			}
			Set<Starship> ships = report.getPlayerStarships();
			if(ships.size() > 0) {
				TreeItem itemShips = new TreeItem(tree,SWT.NONE);
				itemShips.setText("Starships");
				for(Starship ship : ships) {
					TreeItem item = new TreeItem(itemShips,SWT.NONE);
					tree.addListener(SWT.Selection, listenShip(ship));
					item.setText(ship.getName() +" [" + ship.getID() +"]");
					item.setData(ship);
					listItemShips.add(item);
					// TODO context menu with suitable orders for ships
				}
			}
			if(report.getPlayerFacilities().size() > 0) {
				TreeItem itemFacilities = new TreeItem(tree,SWT.NONE);
				itemFacilities.setText("Facilities");	
				Map<AFacilityType,Map<Colony,Set<Facility>>> map = report.getPlayerFacilitiesByTypeAndColony();
				for(AFacilityType type : map.keySet()) {
					TreeItem itemType = new TreeItem(itemFacilities,SWT.NONE);
					itemType.setText(type.getName());
					itemType.setData(type);
					listItemFacilities.add(itemType);
					tree.addListener(SWT.Selection, listenFacilityType(type));
					Map<Colony,Set<Facility>> subMap = map.get(type);
					for(Colony colony : subMap.keySet()) {
						TreeItem itemColony = new TreeItem(itemType,SWT.NONE);
						itemColony.setText(colony.getName() +" [" + colony.getID() +"]");
						itemColony.setData(colony);
						tree.addListener(SWT.Selection, listenColony(colony));
						listItemFacilities.add(itemColony);
						for(Facility facility : subMap.get(colony)) {
							TreeItem item = new TreeItem(itemColony,SWT.NONE);
							tree.addListener(SWT.Selection, listenFacility(facility));
							item.setText(facility.getTypeClass().getName() +" [" + facility.getID() +"]");
							item.setData(facility);
							listItemFacilities.add(item);
							// TODO context menu with suitable orders for facilities
						}
					}
				}
			}
		}
		parent.setVisible(true);
		redraw();
	}
	
	private void dispose(List<TreeItem> list) {
		Iterator<TreeItem> i = list.iterator();
		while(i.hasNext()) {
			TreeItem item = i.next();
			item.dispose();
			i.remove();
		}
	}
	
	public void dispose() {
		dispose(listItemFacilities);
		dispose(listItemDesigns);
		dispose(listItemShips);
		dispose(listItemOrders);
		if(tree != null && !tree.isDisposed())
			tree.dispose();
		parent.dispose();
	}

	public void open(Composite parent) {
		this.parent = new Composite(parent,SWT.NONE);
		this.parent.setVisible(false);
		this.parent.setLayout(new GridLayout(1,false));
		GridData data = new GridData();
		data.verticalAlignment = SWT.TOP;
		data.horizontalAlignment=SWT.LEFT;
		data.minimumWidth=200;
		data.minimumHeight=530;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		parent.setLayoutData(data);
		redraw();
	}
	
	private String getCorporationName() {
		return report == null ? "Corporation" : report.getTurn().getCorporation().getName();
	}
	
	private Listener listenCorporation(final Corporation corp) {
		return new Listener() {
			public void handleEvent(Event event) {
				if(event.item.getData() == corp)
					mainWindow.set(new CorporationPane(mainWindow, corp));
			}
		};
	}

	private Listener listenColony(final Colony colony) {
		return new Listener() {
			public void handleEvent(Event event) {
				if(event.item.getData() == colony)
					mainWindow.set(new ColonyPane(mainWindow, colony));
			}
		};
	}
	
	private Listener listenFacilityType(final AFacilityType type) {
		return new Listener() {
			public void handleEvent(Event event) {
				if(event.item.getData() == type)
					mainWindow.set(new FacilityTypePane(mainWindow, type));
			}
		};
	}
	
	private Listener listenOrder(final TurnOrder order) {
		return new Listener() {
			public void handleEvent(Event event) {
				if(event.item.getData() == order)
					mainWindow.set(new OrderReportPane(mainWindow, order));
			}
		};
	}
	
	private Listener listenReport(final TurnReport report) {
		return new Listener() {
			public void handleEvent(Event event) {
				if(event.item.getData() == report)
					mainWindow.set(new TurnReportPane(mainWindow, report));
			}
		};
	}

	private Listener listenShip(final Starship ship) {
		return new Listener() {
			public void handleEvent(Event event) {
				if(event.item.getData() == ship)
					mainWindow.set(new StarshipPane(mainWindow, ship));
			}
		};
	}
	
	private Listener listenDesign(final StarshipDesign design) {
		return new Listener() {
			public void handleEvent(Event event) {
				if(event.item.getData() == design)
					mainWindow.set(new StarshipDesignPane(mainWindow, design));
			}
		};
	}

	private Listener listenFacility(final Facility facility) {
		return new Listener() {
			public void handleEvent(Event event) {
				if(event.item.getData() == facility)
					mainWindow.set(new FacilityPane(mainWindow, facility));
			}
		};
	}
	
	public Point computeSize() {
		return tree.computeSize(200,450);
	}

	public void redraw() {
		parent.pack();
		parent.redraw();
	}

}
