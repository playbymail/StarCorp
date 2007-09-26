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
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

/**
 * starcorp.client.gui.ATablePane
 * 
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 25 Sep 2007
 */
public abstract class ATablePane extends AWindowPane {

	private Table table;
	private List<TableColumn> columns = new ArrayList<TableColumn>();
	private List<TableItem> items = new ArrayList<TableItem>();
	
	public ATablePane(AWindow mainWindow) {
		super(mainWindow);
	}
	
	public void dispose() {
		for(TableItem item : items) {
			if(!item.isDisposed())
				item.dispose();
		}
		for(TableColumn c : columns) {
			if(!c.isDisposed())
				c.dispose();
		}
		table.dispose();
		super.dispose();
	}
	
	protected int getTableStyle() {
		return SWT.SINGLE | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
	}
	
	protected boolean isHeaderVisible() {
		return true;
	}
	
	protected boolean isLinesVisible() {
		return true;
	}
	

	protected void createWidgets(List<Widget> widgets) {
		table = new Table(getParent(),getTableStyle());
		table.setHeaderVisible(isHeaderVisible());
		table.setLinesVisible(isLinesVisible());
		
		for(int i = 0; i < countColumns(); i++) {
			TableColumn col = new TableColumn(table, SWT.CENTER);
			col.setText(getColumnName(i));
			columns.add(col);
			col.pack();
		}
		populate();
		
		final TableEditor editor = new TableEditor(table);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent event) {
				// Dispose any existing editor
		        Control old = editor.getEditor();
		        if (old != null) old.dispose();

		        // Determine where the mouse was clicked
		        Point pt = new Point(event.x, event.y);

		        // Determine which row was selected
		        final TableItem item = table.getItem(pt);
		        if (item != null) {
		        	// Determine which column was selected
		        	int column = -1;
		        	for (int i = 0, n = table.getColumnCount(); i < n; i++) {
		        		Rectangle rect = item.getBounds(i);
		        		if (rect.contains(pt)) {
		        			// This is the selected column
		        			column = i;
		        			break;
		        		}
		        	}
		        	columnSelected(editor, item,column);
		        }
			}
		});
	}

	protected void columnSelected(TableEditor editor, TableItem row, int column) {
		
	}
	
	protected void createRow() {
		final TableItem item = new TableItem(table,SWT.NONE);
		items.add(item);
	}
	
	protected void deleteRow(int row) {
		final TableItem item = items.get(row);
		table.remove(row);
		if(!item.isDisposed())
			item.dispose();
		items.remove(row);
	}
	
	protected void createTableEditor(Control c, int row, int column) {
		final TableItem item = items.get(row);
		final TableEditor editor = new TableEditor(table);
		editor.grabHorizontal=true;
		editor.minimumHeight=c.getSize().y;
		editor.minimumWidth=c.getSize().x;
		editor.setEditor(c,item,column);
	}
	
	protected void populate() {}
	
	protected abstract int countColumns();
	
	protected abstract String getColumnName(int index);

	public void redraw() {
		table.pack();
	}

	public Point computeSize() {
		return table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}

}
