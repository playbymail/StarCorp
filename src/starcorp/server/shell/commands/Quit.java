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

import starcorp.server.shell.ACommand;

/**
 * starcorp.server.shell.commands.Quit
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 20 Sep 2007
 */
public class Quit extends ACommand {

	/* (non-Javadoc)
	 * @see starcorp.server.shell.ACommand#process()
	 */
	@Override
	public void process() throws Exception {
		out.println("Bye!");
		out.flush();
		System.exit(0);
	}

	@Override
	public String getName() {
		return "quit";
	}

	@Override
	public String getHelpText() {
		return "Exits the server shell.";
	}

}
