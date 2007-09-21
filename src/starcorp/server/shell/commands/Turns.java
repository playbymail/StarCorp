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

import starcorp.server.entitystore.IEntityStore;
import starcorp.server.shell.ACommand;
import starcorp.server.turns.TurnFetcher;
import starcorp.server.turns.TurnProcessor;

/**
 * starcorp.server.shell.commands.Turns
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 20 Sep 2007
 */
public class Turns extends ACommand {

	private TurnProcessor turns;
	private TurnFetcher fetcher = new TurnFetcher();
	
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

	/* (non-Javadoc)
	 * @see starcorp.server.shell.ACommand#process()
	 */
	@Override
	public void process() throws Exception {
		fetcher.fetchTurns();
		out.println(fetcher.getFetched() + " turns fetched.");
		out.flush();
		turns.processTurns();
		out.println(turns.getProcessed() +  " turns processed.");
		out.flush();
	}

	@Override
	public void setEntityStore(IEntityStore entityStore) {
		super.setEntityStore(entityStore);
		turns = new TurnProcessor(entityStore);
	}

}
