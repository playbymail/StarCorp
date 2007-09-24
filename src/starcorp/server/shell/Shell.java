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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import starcorp.common.types.GalacticDate;
import starcorp.server.ServerConfiguration;
import starcorp.server.engine.AServerTask;
import starcorp.server.engine.ServerEngine;
import starcorp.server.entitystore.HibernateStore;
import starcorp.server.entitystore.IEntityStore;

/**
 * starcorp.server.shell.Shell
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 20 Sep 2007
 */
public class Shell {
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		new Shell();
		
	}
	private static final GalacticDate date = ServerConfiguration.getCurrentDate();
	public static final String PROMPT = date + ">";

	private final ServerEngine engine;
	private final CommandParser parser;
	private final Log log = LogFactory.getLog(Shell.class);
	private final IEntityStore entityStore;
	private final BufferedReader input;
	
	public Shell() {
		entityStore = new HibernateStore();
		engine = new ServerEngine(entityStore);
		parser = new CommandParser();
		input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Welcome to Starcorp server!");
		System.out.flush();
		while(true) {
			processInput();
			Thread.yield();
		}
	}
	
	private void processInput() {
		String text = readCommandLine();
		try {
			processCommandLine(text);
		}
		catch(Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	private void processCommandLine(String text) {
		AServerTask task = parser.parse(text);
		if(task == null) {
			return;
		}
		try {
			engine.schedule(task);
			
		} catch (Throwable e) {
			System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
			log.error(e.getMessage(),e);
		}
	}
	
	private String readCommandLine() {
		System.out.print(PROMPT);
		System.out.flush();
		String line = null;
		do {
			try {
				line = input.readLine();
			} catch (IOException e) {
				log.error(e.getMessage(),e);
			}
		}
		while(line == null);
		return line;
	}
	
}
