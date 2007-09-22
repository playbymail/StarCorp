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

import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import starcorp.server.engine.AServerTask;
import starcorp.server.shell.ACommand;
import starcorp.server.shell.Shell;
import starcorp.server.turns.TurnFetcher;
import starcorp.server.turns.TurnProcessor;

/**
 * starcorp.server.shell.commands.Turns
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 20 Sep 2007
 */
public class Turns extends ACommand {
	private static Log log = LogFactory.getLog(Turns.class); 

	/* (non-Javadoc)
	 * @see starcorp.server.shell.ACommand#getHelpText()
	 */
	@Override
	public String getHelpText() {
		return "turns\n\nFetches turns using the TurnFetcher and processes the turns using the TurnProcessor.";
	}

	/* (non-Javadoc)
	 * @see starcorp.server.shell.ACommand#getName()
	 */
	@Override
	public String getName() {
		return "turns";
	}

	public AServerTask task(final Arguments args, final PrintWriter out) {
		return new AServerTask() {
			protected String getName() {
				return "turns";
			}
			protected Log getLog() {
				return log;
			}
			protected void doJob() throws Exception {
				TurnFetcher fetcher = new TurnFetcher();
				TurnProcessor turns = new TurnProcessor(entityStore);
				fetcher.fetchTurns();
				out.println();
				out.println(fetcher.getFetched() + " turns fetched.");
				out.flush();
				turns.processTurns();
				out.println();
				out.println(turns.getProcessed() +  " turns processed.");
				out.print(Shell.PROMPT);
				out.flush();
			}
		};
	}
}
