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
import starcorp.server.facilities.FacilityProcessor;
import starcorp.server.population.PopulationProcessor;
import starcorp.server.shell.ACommand;
import starcorp.server.shell.Shell;

/**
 * starcorp.server.shell.commands.Updater
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 20 Sep 2007
 */
public class Updater extends ACommand {
	private static Log log = LogFactory.getLog(Updater.class); 

	
	public AServerTask task(final Arguments args, final PrintWriter out) {
		return new AServerTask() {
			protected String getName() {
				return "updater";
			}
			protected Log getLog() {
				return log;
			}
			protected void doJob() throws Exception {
				PopulationProcessor popUpdate = new PopulationProcessor(entityStore);
				FacilityProcessor facUpdate = new FacilityProcessor(entityStore);
				
				popUpdate.process();
				out.println();
				out.println("Population updated.");
				out.println(Shell.PROMPT);
				out.flush();
				facUpdate.process();
				out.println();
				out.println("Facilities updated.");
				out.print(Shell.PROMPT);
				out.flush();
			}
		};
	}

	@Override
	public String getName() {
		return "update";
	}

	@Override
	public String getHelpText() {
		return "update\n\nRuns the PopulationProcessor and the FacilityProcessor.";
	}

}
