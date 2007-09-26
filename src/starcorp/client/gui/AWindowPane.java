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
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import starcorp.client.gui.panes.ColonyPane;
import starcorp.client.gui.panes.FacilityTypePane;
import starcorp.client.gui.panes.ItemPane;
import starcorp.client.gui.panes.StarshipDesignPane;
import starcorp.client.gui.panes.SystemEntityPane;
import starcorp.client.gui.widgets.Hyperlink;
import starcorp.common.entities.Colony;
import starcorp.common.entities.IEntity;
import starcorp.common.entities.Planet;
import starcorp.common.entities.StarshipDesign;
import starcorp.common.types.ABaseType;
import starcorp.common.types.AFacilityType;
import starcorp.common.types.AItemType;
import starcorp.common.types.Items;

/**
 * starcorp.client.gui.AWindowPane
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 26 Sep 2007
 */
public abstract class AWindowPane implements IComponent {
	
	private Group panel;
	protected final AWindow window;
	private List<Widget> widgets = new ArrayList<Widget>();
	
	public AWindowPane(AWindow window) {
		this.window = window;
	}

	public Point computeSize() {
		return panel.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}

	public void dispose() {
		for(Widget w : widgets) {
			if(!w.isDisposed())
				w.dispose();
		}
		panel.dispose();
	}

	public void open(Composite parent) {
		panel = new Group(parent, SWT.NONE);
		createWidgets(widgets);
	}
	
	protected Group getParent() {
		return panel;
	}
	
	protected abstract void createWidgets(List<Widget> widgets);

	public void redraw() {
		panel.pack();
		panel.redraw();
	}
	
	protected Hyperlink createFacilityTypeLink(Composite parent, List<Widget> widgets, final AFacilityType type, String label) {
		if(label == null) {
			label = type.getName();
		}
		Hyperlink lnk = createHyperlink(parent, widgets, label);
		lnk.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				window.getMainWindow().set(new FacilityTypePane(window.getMainWindow(),type));
				window.getMainWindow().focus();
			}
			public void widgetSelected(SelectionEvent e) {
				window.getMainWindow().set(new FacilityTypePane(window.getMainWindow(),type));
				window.getMainWindow().focus();
			}
		});
		return lnk;
	}

	protected Hyperlink createItemLink(Composite parent, List<Widget> widgets, final Items item, String label) {
		if(label == null) {
			label = item.toString();
		}
		return createItemLink(parent, widgets, item.getTypeClass(), label);
	}
	
	protected Hyperlink createItemLink(Composite parent, List<Widget> widgets, final AItemType type, String label) {
		if(label == null) {
			label = type.getName();
		}
		Hyperlink lnk = createHyperlink(parent, widgets, label);
		lnk.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				window.getMainWindow().set(new ItemPane(window.getMainWindow(),type));
				window.getMainWindow().focus();
			}
			public void widgetSelected(SelectionEvent e) {
				window.getMainWindow().set(new ItemPane(window.getMainWindow(),type));
				window.getMainWindow().focus();
			}
		});
		return lnk;
	}
	
	protected Hyperlink createPlanetLink(Composite parent, List<Widget> widgets, final Planet planet, String label) {
		if(label == null) {
			label = planet.getDisplayName();
		}
		Hyperlink lnk = createHyperlink(parent, widgets, label);
		lnk.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				window.getMainWindow().set(new SystemEntityPane(window.getMainWindow(),planet));
				window.getMainWindow().focus();
			}
			public void widgetSelected(SelectionEvent e) {
				window.getMainWindow().set(new SystemEntityPane(window.getMainWindow(),planet));
				window.getMainWindow().focus();
			}
		});
		return lnk;
	}
	
	protected Hyperlink createColonyLink(Composite parent, List<Widget> widgets, final Colony colony, String label) {
		if(label == null) {
			label = colony.getDisplayName();
		}
		Hyperlink lnk = createHyperlink(parent, widgets, label);
		lnk.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				window.getMainWindow().set(new ColonyPane(window.getMainWindow(),colony));
				window.getMainWindow().focus();
			}
			public void widgetSelected(SelectionEvent e) {
				window.getMainWindow().set(new ColonyPane(window.getMainWindow(),colony));
				window.getMainWindow().focus();
			}
		});
		return lnk;
	}
	
	protected Hyperlink createDesignLink(Composite parent, List<Widget> widgets, final StarshipDesign design, String label) {
		if(label == null) {
			label = design.getDisplayName();
		}
		Hyperlink lnk = createHyperlink(parent, widgets, label);
		lnk.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				window.getMainWindow().set(new StarshipDesignPane(window.getMainWindow(),design));
				window.getMainWindow().focus();
			}
			public void widgetSelected(SelectionEvent e) {
				window.getMainWindow().set(new StarshipDesignPane(window.getMainWindow(),design));
				window.getMainWindow().focus();
			}
		});
		return lnk;
	}
	
	protected Hyperlink createHyperlink(Composite parent, List<Widget> widgets, String label) {
		Hyperlink lnk = new Hyperlink(parent, SWT.PUSH);
		if(label != null) {
			lnk.setText(label);
		}
		widgets.add(lnk);
		return lnk;
	}

	protected Button createButton(Composite parent, List<Widget> widgets, String label) {
		Button btn = new Button(parent, SWT.PUSH);
		if(label != null) {
			btn.setText(label);
		}
		widgets.add(btn);
		return btn;
	}
	
	protected Group createGroup(Composite parent, List<Widget> widgets, String label) {
		Group grp = new Group(parent,SWT.NONE);
		if(label != null) {
			grp.setText(label);
		}
		widgets.add(grp);
		return grp;
	}
	
	protected Label createLabel(Composite parent, List<Widget> widgets, String label) {
		if(label != null) {
//			System.out.println("Creating label " + label);
			Label lbl = new Label(parent, SWT.NONE);
			lbl.setText(label);
			widgets.add(lbl);
			return lbl;
		}
		return null;
	}
	
	protected Text createTextInput(Composite parent, List<Widget> widgets, String label) {
		createLabel(parent, widgets, label);
		Text txt = new Text(parent, SWT.BORDER);
		RowData data = new RowData(100,SWT.DEFAULT);
		txt.setLayoutData(data);
		widgets.add(txt);
		
		return txt;
	}

	protected Text createMultilineTextInput(Composite parent, List<Widget> widgets, String label) {
		createLabel(parent, widgets, label);
		Text txt = new Text(parent, SWT.BORDER | SWT.MULTI);
		RowData data = new RowData(100,100);
		txt.setLayoutData(data);
		widgets.add(txt);
		
		return txt;
	}

	protected Text createIntegerInput(Composite parent, List<Widget> widgets, String label) {
		final Text txt = createTextInput(parent, widgets, label);
		txt.addListener(SWT.Verify, new Listener() {
		      public void handleEvent(Event e) {
		          String string = e.text;
		          char[] chars = new char[string.length()];
		          string.getChars(0, chars.length, chars, 0);
		          for (int i = 0; i < chars.length; i++) {
		            if (!('0' <= chars[i] && chars[i] <= '9')) {
		              e.doit = false;
		              return;
		            }
		          }
		        }
		      });
		return txt;
	}

	protected List<Object> getListValues(org.eclipse.swt.widgets.List list) {
		List<Object> retval = new ArrayList<Object>();
		Collection<?> data = (Collection<?>) list.getData(); 
		int[] indices = list.getSelectionIndices();
		int selected = indices == null ? 0 : indices.length;
		int i = 0;
		for(Object o : data) {
			for(int j = 0; j < selected; j++) {
				if(indices[j] == i) {
					retval.add(o);
				}
			}
			i++;
		}
		return retval;
	}
	
	protected Object getComboValue(Combo c) {
		int index = c.getSelectionIndex();
		int i = 0;
		for(Object o : (Collection<?>) c.getData()) {
			if(i == index)
				return o;
			i++;
		}
		return null;
	}
	
	protected Combo createCombo(Composite parent, List<Widget> widgets, String[] items, Object values, String label) {
		createLabel(parent, widgets, label);
		final Combo c = new Combo(parent,SWT.DROP_DOWN | SWT.READ_ONLY);
		for(String s : items) {
			c.add(s);
			System.out.println(s);
		}
		c.setData(values);
		RowData data = new RowData(SWT.DEFAULT,70);
		c.setLayoutData(data);
		widgets.add(c);
		return c;
	}

	protected org.eclipse.swt.widgets.List createMultiList(Composite parent, List<Widget> widgets, String[] items, Object values, String label) {
		createLabel(parent, widgets, label);
		final org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(parent,SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		list.setItems(items);
		list.setData(values);
		RowData data = new RowData(SWT.DEFAULT,70);
		list.setLayoutData(data);
		widgets.add(list);
		return list;
	}

	protected Combo createTypeSelection(Composite parent, List<Widget> widgets, List<?> types, String label) {
		String[] items = new String[types.size()];
		TreeSet<ABaseType> set = new TreeSet<ABaseType>();
		for(Object o : types) {
			ABaseType type = (ABaseType) o;
			set.add(type);
		}
		int i = 0;
		for(ABaseType type : set) {
			items[i] = type.getName();
			i++;
		}
		return createCombo(parent, widgets, items, set, label);
	}
	
	protected org.eclipse.swt.widgets.List createTypeMultiSelection(Composite parent, List<Widget> widgets, List<?> types, String label) {
		String[] items = new String[types.size()];
		TreeSet<ABaseType> set = new TreeSet<ABaseType>();
		for(Object o : types) {
			ABaseType type = (ABaseType) o;
			set.add(type);
		}
		int i = 0;
		for(ABaseType type : set) {
			items[i] = type.getName();
			i++;
		}
		return createMultiList(parent, widgets, items, set, label);
		
	}

	protected Combo createEntitySelection(Composite parent, List<Widget> widgets, List<?> types, String label) {
		String[] items = new String[types.size()];
		TreeSet<IEntity> set = new TreeSet<IEntity>();
		for(Object o : types) {
			IEntity type = (IEntity) o;
			set.add(type);
		}
		int i = 0;
		for(IEntity type : set) {
			items[i] = type.getDisplayName();
			i++;
		}
		return createCombo(parent, widgets, items, set, label);
	}
	
	protected org.eclipse.swt.widgets.List createEntityMultiSelection(Composite parent, List<Widget> widgets, List<?> types, String label) {
		String[] items = new String[types.size()];
		TreeSet<IEntity> set = new TreeSet<IEntity>();
		for(Object o : types) {
			IEntity type = (IEntity) o;
			set.add(type);
		}
		int i = 0;
		for(IEntity type : set) {
			items[i] = type.getDisplayName();
			i++;
		}
		return createMultiList(parent, widgets, items, set, label);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((window == null) ? 0 : window.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AWindowPane other = (AWindowPane) obj;
		if (window == null) {
			if (other.window != null)
				return false;
		} else if (!window.equals(other.window))
			return false;
		return true;
	}

}
