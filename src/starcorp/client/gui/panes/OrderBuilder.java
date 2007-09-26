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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import starcorp.client.gui.ABuilderPane;
import starcorp.client.gui.ADataEntryWindow;
import starcorp.common.entities.IEntity;
import starcorp.common.turns.Turn;
import starcorp.common.turns.TurnOrder;
import starcorp.common.turns.TurnReport;
import starcorp.common.types.ABaseType;
import starcorp.common.types.AFacilityType;
import starcorp.common.types.AItemType;
import starcorp.common.types.ColonyHub;
import starcorp.common.types.Factory;
import starcorp.common.types.OrderType;
import starcorp.common.types.PopulationClass;
import starcorp.common.types.StarshipHulls;

/**
 * starcorp.client.gui.OrderBuilder
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 25 Sep 2007
 */
public class OrderBuilder extends ABuilderPane {

	private final Turn turn;
	private final TurnReport report;
	private Group[] orderArgumentPanels = new Group[5];
	private Widget[] orderArgumentContent = new Widget[5];
	
	public OrderBuilder(ADataEntryWindow mainWindow) {
		super(mainWindow);
		report = mainWindow.getMainWindow().getTurnReport();
		turn = mainWindow.getMainWindow().getCurrentTurn();
	}

	@Override
	protected void createWidgets(List<Widget> widgets) {
		getParent().setText("Order Builder");
		getParent().setLayout(new RowLayout(SWT.HORIZONTAL));
		
		Group buttonPanel = createGroup(getParent(), widgets, null);
		buttonPanel.setLayout(new RowLayout(SWT.VERTICAL));
		
		Button add = createButton(buttonPanel, widgets, "Add");
		// TODO replace text for button with an icon + tooltip
		add.addListener(SWT.Selection, new Listener() {
			public void handleEvent (Event event) {
				TurnOrder order = readOrder();
				turn.add(order);
				mainWindow.getMainWindow().setTurnDirty(true);
			}
		});

		Group orderSelectionPanel = createGroup(getParent(), widgets, "Type");
		orderSelectionPanel.setLayout(new RowLayout(SWT.VERTICAL));
		for(int i = 0; i < 5; i++) {
			orderArgumentPanels[i] = new Group(getParent(),SWT.NONE);
			orderArgumentPanels[i].setLayout(new RowLayout(SWT.VERTICAL));
			orderArgumentPanels[i].setVisible(false);
		}
		
		createOrderTypeSelection(orderSelectionPanel,widgets);
	}
	
	private Combo orderTypeSelector;

	private void createOrderTypeSelection(Group orderSelectionPanel, final List<Widget> widgets) {
		List<String> types = new ArrayList<String>();
		types.addAll(OrderType.setOrders());
		String[] items = new String[types.size()];
		for(int i = 0; i < items.length; i++) {
			items[i] = types.get(i);
		}
		orderTypeSelector = createCombo(orderSelectionPanel, widgets, items, types, null);
		orderTypeSelector.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				chooseOrderType(widgets, (String)getComboValue(orderTypeSelector)); 
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				chooseOrderType(widgets, (String)getComboValue(orderTypeSelector));
			}
		});
	}

	private void chooseOrderType(List<Widget> widgets, String type) {
		System.out.println(type + " selected");
		clearAllContents();
		if(type.equals(OrderType.BUILD_FACILITY)){
			// TODO filter facility types to colonies building modules available
			createColonyDropDown(widgets, 0);
			createFacilityTypeDropDown(widgets, 1);
		}
		else if(type.equals(OrderType.BUILD_STARSHIP)){
			// TODO filter ship designs by colony items for given colony
			createColonyDropDown(widgets, 0);
			createShipDesignDropDown(widgets, 1);
		}
		else if(type.equals(OrderType.ISSUE_COLONIST_GRANT)){
			// TODO filter colonies to those which player is government
			createColonyDropDown(widgets, 0);
			createPopClassDropDown(widgets, 1);
			createIntegerInput(widgets, 2, "Credits");
		}
		else if(type.equals(OrderType.ISSUE_DEVELOPMENT_GRANT)){
			// TODO filter colonies to those which player is government
			createColonyDropDown(widgets, 0);
			createFacilityTypeDropDown(widgets, 1);
			createIntegerInput(widgets, 2, "Credits");
		}
		else if(type.equals(OrderType.ISSUE_LEASE)){
			// TODO filter colonies to those which player is government
			createColonyDropDown(widgets, 0);
			createFacilityTypeDropDown(widgets, 1);
			createIntegerInput(widgets, 2, "Price");
		}
		else if(type.equals(OrderType.FACTORY_BUILD)){
			createFactoryDropDown(widgets, 0);
			createItemTypeDropDown(widgets, 1);
			createIntegerInput(widgets, 2, "Quantity");
		}
		else if(type.equals(OrderType.SET_SALARY)){
			createFacilityDropDown(widgets, 0);
			createPopClassDropDown(widgets, 1);
			createIntegerInput(widgets, 2, "Salary");
		}
		else if(type.equals(OrderType.CORP_BUY_ITEM)){
			createColonyDropDown(widgets, 0);
			createItemTypeDropDown(widgets, 1);
			createIntegerInput(widgets, 2, "Quantity");
		}
		else if(type.equals(OrderType.CORP_SELL_ITEM)){
			createColonyDropDown(widgets, 0);
			createItemTypeDropDown(widgets, 1);
			createIntegerInput(widgets, 2, "Quantity");
			createIntegerInput(widgets, 3, "Price");
		}
		else if(type.equals(OrderType.SHIP_BUY_ITEM)){
			// TODO filter ships to those at a colony or in orbit of a planet
			// TODO filter colony drop down based on ship selection
			createShipDropDown(widgets, 0);
			createColonyDropDown(widgets, 1);
			createItemTypeDropDown(widgets, 2);
			createIntegerInput(widgets, 3, "Quantity");
		}
		else if(type.equals(OrderType.SHIP_SELL_ITEM)){
			// TODO filter ships to those at a colony or in orbit of a planet
			// TODO filter colony drop down based on ship selection
			createShipDropDown(widgets, 0);
			createColonyDropDown(widgets, 1);
			createItemTypeDropDown(widgets, 2);
			createIntegerInput(widgets, 3, "Quantity");
			createIntegerInput(widgets, 4, "Price");
		}
		else if(type.equals(OrderType.SHIP_PICKUP_ITEM)){
			// TODO filter ships to those at a colony or in orbit of a planet
			// TODO filter colony drop down based on ship selection
			createColonyDropDown(widgets, 0);
			createItemTypeDropDown(widgets, 1);
			createIntegerInput(widgets, 2, "Quantity");
		}
		else if(type.equals(OrderType.SHIP_DELIVER_ITEM)){
			// TODO filter ships to those at a colony or in orbit of a planet
			// TODO filter colony drop down based on ship selection
			createColonyDropDown(widgets, 0);
			createItemTypeDropDown(widgets, 1);
			createIntegerInput(widgets, 2, "Quantity");
		}
		else if(type.equals(OrderType.FOUND_COLONY)){
			// TODO filter to ships landed on a planet
			createShipDropDown(widgets, 0);
			createTextInput(widgets, 1, "Name");
		}
		else if(type.equals(OrderType.INVESTIGATE)){
			// TODO filter to ships in same location as a known anomoly
			createShipDropDown(widgets, 0);
			createAnomolyDropDown(widgets, 1);
		}
		else if(type.equals(OrderType.LEAVE_ORBIT)){
			// TODO filter to ships in orbit of a planet
			createShipDropDown(widgets, 0);
		}
		else if(type.equals(OrderType.ORBIT)){
			// TODO filter to ships in space
			// TODO filter planets based on ship selected
			createShipDropDown(widgets, 0);
			createPlanetDropDown(widgets, 1);
		}
		else if(type.equals(OrderType.DOCK_COLONY)){
			// TODO filter to ships in orbit
			// TODO filter colonies based on ship selected
			createShipDropDown(widgets, 0);
			createColonyDropDown(widgets, 1);
		}
		else if(type.equals(OrderType.DOCK_PLANET)){
			// TODO filter to ships in orbit
			createShipDropDown(widgets, 0);
			createIntegerInput(widgets, 1, "X");
			createIntegerInput(widgets, 2, "Y");
		}
		else if(type.equals(OrderType.TAKE_OFF)){
			// TODO filter to landed ships
			createShipDropDown(widgets, 0);
		}
		else if(type.equals(OrderType.MINE_ASTEROID)){
			// TODO filter to ships in same location as a known asteroid
			createShipDropDown(widgets, 0);
			createAsteroidDropDown(widgets, 1);
		}
		else if(type.equals(OrderType.MINE_GAS_FIELD)){
			// TODO filter to ships in same location as a known gas field
			createShipDropDown(widgets, 0);
			createGasFieldDropDown(widgets, 1);
		}
		else if(type.equals(OrderType.PROBE_PLANET)){
			// TODO filter to ships in orbit
			createShipDropDown(widgets, 0);
			createPlanetDropDown(widgets, 1);
		}
		else if(type.equals(OrderType.PROBE_SYSTEM)){
			// TODO filter to ships in space
			createShipDropDown(widgets, 0);
		}
		else if(type.equals(OrderType.SCAN_GALAXY)){
			// TODO filter to ships in space
			createShipDropDown(widgets, 0);
		}
		else if(type.equals(OrderType.SCAN_SYSTEM)){
			// TODO filter to ships in space
			createShipDropDown(widgets, 0);
		}
		else if(type.equals(OrderType.PROSPECT)){
			// TODO filter to ships landed on a planet
			createShipDropDown(widgets, 0);
		}
		else if(type.equals(OrderType.DESIGN_SHIP)){
			createTextInput(widgets, 0, "Name");
			createShipHullsList(widgets, 1);
			
		}
		else if(type.equals(OrderType.MOVE)){
			// TODO filter to ships in space
			createShipDropDown(widgets, 0);
			createIntegerInput(widgets, 1, "Quadrant");
			createIntegerInput(widgets, 2, "Orbit");
		}
		else if(type.equals(OrderType.JUMP)){
			// TODO filter to ships in space
			createShipDropDown(widgets, 0);
			createStarSystemDropDown(widgets, 1);
		}
	}
	
	private TurnOrder readOrder() {
		TurnOrder order = new TurnOrder();
		order.setCorp(turn.getCorporation());
		order.setType(OrderType.getType(getOrderSelected()));
		
		for(int i = 0; i < orderArgumentContent.length; i++) {
			readContents(order, i);
		}
		return order;
	}
	
	private String getOrderSelected() {
		Object o = getComboValue(orderTypeSelector);
		return o == null ? "" : o.toString();
	}
	
	private void clearAllContents() {
		for(int i = 0; i < orderArgumentPanels.length; i++ ) {
			clearContents(i);
		}
	}
	
	private void clearContents(int index) {
		if(orderArgumentContent[index] != null && !orderArgumentContent[index].isDisposed())
			orderArgumentContent[index].dispose();
		orderArgumentContent[index] = null;
		orderArgumentPanels[index].setVisible(false);
		redraw();
	}
	
	private void setContents(int index, Control w, String label) {
		clearContents(index);
		orderArgumentContent[index] = w;
		orderArgumentPanels[index].setText(label);
		orderArgumentPanels[index].setVisible(true);
	}
	
	private TurnOrder readContents(TurnOrder order, int index) {
		Widget w = orderArgumentContent[index];
		if(w == null)
			return null;
		if(w instanceof Combo) {
			Object o = getComboValue((Combo)w);
			String val = null;
			if(o instanceof IEntity) {
				IEntity entity = (IEntity) o;
				val = String.valueOf(entity.getID());
			}
			else if(o instanceof ABaseType) {
				ABaseType type = (ABaseType) o;
				val = type.getKey();
			}
			else if(o != null){
				val = o.toString();
			}
			order.add(val);
		}
		if(w instanceof org.eclipse.swt.widgets.List) {
			List<Object> list = getListValues((org.eclipse.swt.widgets.List)w);
			for(Object o : list) {
				String val = null;
				if(o instanceof IEntity) {
					IEntity entity = (IEntity) o;
					val = String.valueOf(entity.getID());
				}
				else if(o instanceof ABaseType) {
					ABaseType type = (ABaseType) o;
					val = type.getKey();
				}
				else if(o != null){
					val = o.toString();
				}
				order.add(val);
			}
		}
		if(w instanceof Text) {
			String s = ((Text)w).getText();
			order.add(s);
		}
		return null;
	}
	
	private void createErrorLabel(List<Widget> widgets, int index, String label, String error) {
		Label lbl = new Label(orderArgumentPanels[index],SWT.NONE);
		lbl.setText(error);
		widgets.add(lbl);
		setContents(index, lbl, label);
	}
	
	private void createColonyDropDown(List<Widget> widgets, int index) {
		if(report != null)
			setContents(index,createEntitySelection(orderArgumentPanels[index], widgets, report.getKnownColonies(), null),"Colony");
		else
			createErrorLabel(widgets, index, "Colony", "No report loaded");
	}
	
	private void createFacilityDropDown(List<Widget> widgets, int index) {
		if(report != null)
			setContents(index,createEntitySelection(orderArgumentPanels[index], widgets, report.getPlayerFacilities(), null),"Facility");
		else
			createErrorLabel(widgets, index, "Facility", "No report loaded");
	}
	
	private void createFactoryDropDown(List<Widget> widgets, int index) {
		// TODO make this drop down filterable by colony
		if(report != null)
			setContents(index,createEntitySelection(orderArgumentPanels[index], widgets, report.getPlayerFacilitiesByType(Factory.class), null),"Factory");
		else
			createErrorLabel(widgets, index, "Factory", "No report loaded");
	}

	private void createFacilityTypeDropDown(List<Widget> widgets, int index) {
		setContents(index,createTypeSelection(orderArgumentPanels[index], widgets, AFacilityType.listTypesExcludeClass(ColonyHub.class), null),"Facility");
	}
	
	private void createShipHullsList(List<Widget> widgets, int index) {
		setContents(index,createTypeMultiSelection(orderArgumentPanels[index], widgets, AItemType.listTypes(StarshipHulls.class), null),"Starship Hulls");
	}
	
	private void createItemTypeDropDown(List<Widget> widgets, int index) {
		setContents(index,createTypeSelection(orderArgumentPanels[index], widgets, AItemType.listTypes(), null),"Item");
	}
	
	private void createPopClassDropDown(List<Widget> widgets, int index) {
		setContents(index,createTypeSelection(orderArgumentPanels[index], widgets, PopulationClass.listTypes(), null),"Population Class");
	}

	private void createShipDropDown(List<Widget> widgets, int index) {
		if(report != null)
			setContents(index,createEntitySelection(orderArgumentPanels[index], widgets, report.getPlayerStarships(), null),"Starship");
		else
			createErrorLabel(widgets, index, "Starship", "No report loaded");
	}
	
	private void createShipDesignDropDown(List<Widget> widgets, int index) {
		// TODO fix this to have a combo showing available hulls and a list showing currently chosen with an option to delete ones in list
		if(report != null)
			setContents(index,createEntitySelection(orderArgumentPanels[index], widgets, report.getPlayerDesigns(), null),"Starship Design");
		else
			createErrorLabel(widgets, index, "Starship Design", "No report loaded");
	}
	
	private void createTextInput(List<Widget> widgets, int index, String label) {
		setContents(index,createTextInput(orderArgumentPanels[index], widgets, null), label);
	}
	
	private void createIntegerInput(List<Widget> widgets, int index, String label) {
		setContents(index,createIntegerInput(orderArgumentPanels[index], widgets, null), label);
	}
	
	private void createStarSystemDropDown(List<Widget> widgets, int index) {
		if(report != null)
			setContents(index,createEntitySelection(orderArgumentPanels[index], widgets, report.getKnownSystems(), null),"Star System");
		else
			createErrorLabel(widgets, index, "Star System", "No report loaded");
	}
	
	private void createPlanetDropDown(List<Widget> widgets, int index) {
		if(report != null)
			setContents(index,createEntitySelection(orderArgumentPanels[index], widgets, report.getScannedPlanets(), null),"Asteroid");
		else
			createErrorLabel(widgets, index, "Planet", "No report loaded");
	}

	private void createAsteroidDropDown(List<Widget> widgets, int index) {
		if(report != null)
			setContents(index,createEntitySelection(orderArgumentPanels[index], widgets, report.getScannedAsteroids(), null),"Asteroid");
		else
			createErrorLabel(widgets, index, "Asteroid", "No report loaded");
	}
	
	private void createGasFieldDropDown(List<Widget> widgets, int index) {
		if(report != null)
			setContents(index,createEntitySelection(orderArgumentPanels[index], widgets, report.getScannedGasFields(), null),"Gas Field");
		else
			createErrorLabel(widgets, index, "Gas Field", "No report loaded");
	}
	
	private void createAnomolyDropDown(List<Widget> widgets, int index) {
		if(report != null)
			setContents(index,createEntitySelection(orderArgumentPanels[index], widgets, report.getScannedAnomolies(), null),"Anomoly");
		else
			createErrorLabel(widgets, index, "Anomoly", "No report loaded");
	}
	
	@Override
	public void redraw() {
		for(Group c : orderArgumentPanels) {
			c.pack();
		}
		super.redraw();
	}
}
