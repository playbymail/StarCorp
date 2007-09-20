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
package starcorp.server.shell;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import starcorp.common.util.PackageExplorer;
import starcorp.server.entitystore.IEntityStore;
import starcorp.server.shell.commands.Echo;
import starcorp.server.shell.commands.Quit;

/**
 * starcorp.server.shell.CommandParser
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 20 Sep 2007
 */
public class CommandParser {

	private final IEntityStore entityStore;
	private Map<String, ACommand> commands = new HashMap<String, ACommand>();
	
	public CommandParser(IEntityStore entityStore) {
		this.entityStore = entityStore;
		initializeCommands();
	}
	
	public Iterator<String> listCommands() {
		return commands.keySet().iterator();
	}
	
	public ACommand getCommand(String command) {
		return commands.get(command);
	}
	
	private void initializeCommands() {
		List<Class> classes;
		try {
			classes = PackageExplorer.getClassesForPackage("starcorp.server.shell.commands");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		for(int i = 0; i < classes.size(); i++) {
			try {
				ACommand command = (ACommand) classes.get(i).newInstance();
				command.setEntityStore(entityStore);
				command.setOutputStream(System.out);
				command.setParser(this);
				commands.put(command.getName(), command);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ACommand parse(String text) {
		if(text == null || (text = text.trim()).length() == 0) {
			return null;
		}
		
		String[] args = text.split(" ");
		String commandName = args[0];
		ACommand command = getCommand(commandName);
		
		if(command == null) {
			return null;
		}
		
		String[] commandArgs = new String[args.length - 1];
		System.arraycopy(args, 1, commandArgs, 0, commandArgs.length);
		command.setArguments(commandArgs);
		
		return command;
	}
	
}