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

/**
 * starcorp.client.gui.SearchLawsWindow
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 25 Sep 2007
 */
public class SearchLawsWindow extends ADataEntryWindow {

	public SearchLawsWindow(MainWindow mainWindow) {
		super(mainWindow);
	}

	@Override
	protected ABuilderPane createBuilder() {
		return new SearchLawBuilder(this);
	}

	@Override
	protected ATablePane createTable() {
		return new LawTable(this);
	}

	@Override
	protected void close() {
		super.close();
		mainWindow.searchLawsWindow = null;
	}
}
