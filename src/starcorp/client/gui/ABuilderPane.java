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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;


import starcorp.common.entities.IEntity;
import starcorp.common.types.ABaseType;

/**
 * starcorp.client.gui.ASearchPane
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 25 Sep 2007
 */
public abstract class ABuilderPane implements IComponent {

	protected final ADataEntryWindow mainWindow;
	
	private Group panel;
	private List<Widget> widgets = new ArrayList<Widget>();
	
	public ABuilderPane(ADataEntryWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
	
	public void dispose() {
		for(Widget w : widgets) {
			if(!w.isDisposed())
				w.dispose();
		}
		panel.dispose();
	}

	public void open(Composite parent) {
		panel = new Group(parent, SWT.SHADOW_NONE);
		createWidgets(widgets);
	}
	
	protected Group getParent() {
		return panel;
	}
	
	protected abstract void createWidgets(List<Widget> widgets);

	public void pack() {
		panel.pack();
	}

	public Point computeSize() {
		return panel.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}
	
	protected Text createTextInput(Composite parent, List<Widget> widgets, String label) {
		if(label != null) {
			Label lbl = new Label(parent, SWT.NONE);
			lbl.setText(label);
			widgets.add(lbl);
		}
		
		Text txt = new Text(parent, SWT.BORDER);
		RowData data = new RowData(100,SWT.DEFAULT);
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
		if(label != null) {
			Label lbl = new Label(parent, SWT.NONE);
			lbl.setText(label);
			widgets.add(lbl);
		}
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
		if(label != null) {
			Label lbl = new Label(parent, SWT.NONE);
			lbl.setText(label);
			widgets.add(lbl);
		}
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
}
