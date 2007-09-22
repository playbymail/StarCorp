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
	public static final String getDisplayDuration(long milliseconds) {
		int seconds = (int) milliseconds / 1000;
		if(seconds < 60) { // less than a minute
			return seconds + "s";
		}
		else if(seconds < 3600){ // less than an hour
			int minutes = (int) (seconds / 60);
			int secs = seconds % (minutes * 60);
			return minutes +"m "+secs+"s";
		}
		else {
			int hours = (int) (seconds / 3600);
			int secs = seconds % (hours * 3600);
			int minutes = (int) (secs / 60);
			secs = seconds % (minutes * 60);
			return hours + "h "+minutes +"m "+secs+"s";
		}
	}
	private final CommandParser parser;
	private final GalacticDate date = ServerConfiguration.getCurrentDate();
	private final Log log = LogFactory.getLog(Shell.class);
	private final IEntityStore entityStore;
	private final BufferedReader input;
	private String prompt;
	
	public Shell() {
		prompt = date + ">";
		entityStore = new HibernateStore();
		parser = new CommandParser(entityStore);
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
		ACommand command = parser.parse(text);
		if(command == null) {
			System.out.println("Unknown command");
			return;
		}
		try {
			long start = System.currentTimeMillis();
			command.process();
			long time = System.currentTimeMillis() - start;
			System.out.println("Done " + getDisplayDuration(time));
			
		} catch (Throwable e) {
			System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
			log.error(e.getMessage(),e);
		}
	}
	
	private String readCommandLine() {
		System.out.print(getPrompt());
		System.out.flush();
		String line = null;
		do {
			try {
				line = input.readLine();
			} catch (IOException e) {
				log.error(e.getMessage(),e);
			}
		}
		while(line == null || line.trim().length() == 0);
		return line;
	}
	
	private String getPrompt() {
		return prompt;
	}
	
}
