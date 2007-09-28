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

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Widget;

import starcorp.client.gui.AEntityPane;
import starcorp.client.gui.widgets.Hyperlink;
import starcorp.client.gui.windows.MainWindow;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.MarketItem;
import starcorp.common.types.AItemType;

/**
 * starcorp.client.gui.CorporationPane
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 25 Sep 2007
 */
public class CorporationPane extends AEntityPane {
	private static final int MARKET_ITEMS_PER_PAGE = 10;
	private final int page;

	private Corporation corp;
	
	public CorporationPane(MainWindow mainWindow, Corporation corp) {
		this(mainWindow,corp,1);
	}
	
	public CorporationPane(MainWindow mainWindow, Corporation corp, int page) {
		super(mainWindow, corp);
		this.corp = corp;
		this.page = page;
	}

	@Override
	protected void createWidgets(List<Widget> widgets) {
		super.createWidgets(widgets);
		createLabel(getParent(), widgets, "Credits:");
		createLabel(getParent(), widgets, "0");// TODO get credit account

		createLabel(getParent(), widgets, "Ships:");
		createLabel(getParent(), widgets, String.valueOf(mainWindow.getTurnReport().countPlayerStarships()));
		
		createLabel(getParent(), widgets, "Facilities:");
		createLabel(getParent(), widgets, String.valueOf(mainWindow.getTurnReport().countPlayerFacilities()));
		
		createMarket(getParent(), widgets);
		
		// TODO add cash transactions
	}

	protected void loadPage(int page) {
		System.out.println("loadPage: " + page);
		window.getMainWindow().set(new CorporationPane(window.getMainWindow(),corp,page));
		window.getMainWindow().focus();
	}

	protected Group createMarket(Composite parent, List<Widget> widgets) {
		Group grp = createGroup(parent, widgets, "Items on Sale");
		grp.setLayout(new GridLayout(3,false));
		
		createLabel(grp, widgets, "Item");
		createLabel(grp, widgets, "Colony");
		createLabel(grp,widgets, "Price (ea.)");
		
		Set<MarketItem> set = getTurnReport().getMarketBySeller().get(corp);
		Iterator<MarketItem> i = set.iterator();
		for(int count = 0; i.hasNext() && count < (MARKET_ITEMS_PER_PAGE * (page - 1));count++) {
			i.next();
		}
		GridData data = new GridData(80,20);
		for(int n = 0; n < MARKET_ITEMS_PER_PAGE; n++) {
			MarketItem item = i.hasNext() ? i.next() : null;
			if(item == null)
				continue;
			AItemType type = item.getItem().getTypeClass();
			String name = item.getItem().getTypeClass().getName();
			String qty = String.valueOf(item.getItem().getQuantity());
			String price = "\u20a1 " + item.getCostPerItem();
			createItemLink(grp, widgets, type, qty + " x " + name);
			createColonyLink(grp, widgets, item.getColony(), null);
			createLabel(grp,widgets, price).setLayoutData(data);
		}
		int totalPages = set.size() / MARKET_ITEMS_PER_PAGE;
		if(set.size() % MARKET_ITEMS_PER_PAGE > 0) totalPages++;
		Group grpPages = createGroup(grp, widgets, "Pages");
		GridLayout pagesLayout = new GridLayout(totalPages,true);
		pagesLayout.makeColumnsEqualWidth=true;
		pagesLayout.marginWidth=5;
		pagesLayout.marginHeight=5;
		pagesLayout.horizontalSpacing=5;
		grpPages.setLayout(pagesLayout);
		data = new GridData(SWT.CENTER,SWT.TOP,true,true);
		data.horizontalSpan=4;
		data.grabExcessHorizontalSpace=true;
		data.horizontalAlignment=SWT.CENTER;
		grpPages.setLayoutData(data);
		for(int n = 1; n <= totalPages; n++) {
			if(n == page) {
				createLabel(grpPages, widgets, String.valueOf(n));
			}
			else {
				Hyperlink lnk = createHyperlink(grpPages, widgets, String.valueOf(n));
				final int selected = n;
				lnk.addSelectionListener(new SelectionListener() {
					public void widgetDefaultSelected(SelectionEvent e) {
						loadPage(selected);
					}
					public void widgetSelected(SelectionEvent e) {
						loadPage(selected);
					}
				});
			}
		}
		return grp;
	}
	
	
}
