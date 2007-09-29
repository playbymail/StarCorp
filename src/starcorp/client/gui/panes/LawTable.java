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

import starcorp.client.gui.ADataEntryWindow;
import starcorp.client.gui.ATablePane;

/**
 * starcorp.client.gui.LawTable
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 25 Sep 2007
 */
public class LawTable extends ATablePane {

	public LawTable(ADataEntryWindow mainWindow) {
		super(mainWindow);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected String getTableName() {
		return "Leases / Grants";
	}

	@Override
	protected int countColumns() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected String getColumnName(int index) {
		// TODO Auto-generated method stub
		return null;
	}

}