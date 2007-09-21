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

import java.io.StringWriter;

import starcorp.common.entities.ABaseEntity;
import starcorp.server.shell.ACommand;

/**
 * starcorp.server.shell.commands.View
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 20 Sep 2007
 */
public class View extends ACommand {

	/* (non-Javadoc)
	 * @see starcorp.server.shell.ACommand#getHelpText()
	 */
	@Override
	public String getHelpText() {
		return "view (Entity Class) (ID) (xml?)\n\nPrints out the specified entity.  If the third parameter is supplied and is \"true\" then the output is done in xml format.";
	}

	/* (non-Javadoc)
	 * @see starcorp.server.shell.ACommand#getName()
	 */
	@Override
	public String getName() {
		return "view";
	}

	/* (non-Javadoc)
	 * @see starcorp.server.shell.ACommand#process()
	 */
	@Override
	public void process() throws Exception {
		String entityClass = get(0);
		int ID = getAsInt(1);
		boolean xml = isTrue(2);
		if(entityClass == null || ID == 0) {
			out.print("Invalid arguments");
		}
		else {
			String className = "starcorp.common.entities." + entityClass;
			Class clazz = Class.forName(className);
			ABaseEntity entity = entityStore.load(clazz, ID);
			if(xml) {
				StringWriter sw = new StringWriter();
				entity.printXML(sw);
				out.println(sw.toString());
			}
			else {
				out.println(entity);
			}
			out.flush();
		}

	}

}
