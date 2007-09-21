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

import starcorp.server.shell.ACommand;

/**
 * starcorp.server.shell.commands.HQL
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 20 Sep 2007
 */
public class HQL extends ACommand {

	/* (non-Javadoc)
	 * @see starcorp.server.shell.ACommand#getHelpText()
	 */
	@Override
	public String getHelpText() {
		return "hql (Query)\n\nPerforms a Hibernate Query using the provided query string.";
	}

	/* (non-Javadoc)
	 * @see starcorp.server.shell.ACommand#getName()
	 */
	@Override
	public String getName() {
		return "hql";
	}

	/* (non-Javadoc)
	 * @see starcorp.server.shell.ACommand#process()
	 */
	@Override
	public void process() throws Exception {
		String hql = concat(0);
		if(hql == null || hql.length() < 1) {
			out.println("Invalid argument.");
		}
		else {
			List<?> list = entityStore.query(hql);
			if(list == null || list.size() < 1) {
				out.println("0 results.");
			}
			else {
				for(Object o : list) {
					out.println(o);
				}
			}
		}
		out.flush();
	}

}
