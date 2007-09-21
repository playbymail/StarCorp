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
package starcorp.server.shell.commands;

import java.util.List;

import starcorp.common.types.AItemType;
import starcorp.server.shell.ACommand;

/**
 * starcorp.server.shell.commands.Items
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 20 Sep 2007
 */
public class Items extends ACommand {

	/* (non-Javadoc)
	 * @see starcorp.server.shell.ACommand#getHelpText()
	 */
	@Override
	public String getHelpText() {
		return "items\nitems [filter]\n\nPrints out item types. Filter is optional and filters items to contain the filter string.";
	}

	/* (non-Javadoc)
	 * @see starcorp.server.shell.ACommand#getName()
	 */
	@Override
	public String getName() {
		return "items";
	}

	/* (non-Javadoc)
	 * @see starcorp.server.shell.ACommand#process()
	 */
	@Override
	public void process() throws Exception {
		String filter = get(0);
		List<AItemType> types;
		if(filter == null) {
			types = AItemType.listTypes();
		}
		else {
			types = AItemType.listTypes(filter);
		}
		out.println("List of matching items:");
		if(types.size() < 1) {
			out.println("None");
		}
		for(AItemType type : types) {
			out.println(type);
		}
		out.println();
		out.flush();
		
	}

}
