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

import java.io.OutputStream;
import java.io.PrintWriter;

import starcorp.server.entitystore.IEntityStore;

/**
 * starcorp.server.shell.ACommand
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 20 Sep 2007
 */
public abstract class ACommand {

	private String[] args;
	protected CommandParser parser;
	protected PrintWriter out;
	protected IEntityStore entityStore;
	
	public abstract String getName();
	public abstract String getHelpText();
	public abstract void process() throws Exception;
	
	public void setParser(CommandParser parser) {
		this.parser = parser;
	}
	
	public void setArguments(String[] args) {
		this.args = args;
	}
	
	public void setOutputStream(OutputStream out) {
		this.out = new PrintWriter(out);
	}
	
	public void setEntityStore(IEntityStore entityStore) {
		this.entityStore = entityStore;
	}
	
	protected int count() {
		return args.length;
	}
	
	protected String get(int index) {
		if(index >= args.length)
			return null;
		return args[index];
	}
	
	protected int getAsInt(int index) {
		String s = get(index);
		if(s == null)
			return 0;
		try {
			return Integer.parseInt(args[index]);
		}
		catch(NumberFormatException e) {
			return 0;
		}
	}
	
	protected boolean isTrue(int index) {
		String s = get(index);
		if(s == null)
			return false;
		return "true".equalsIgnoreCase(s);
	}
	
	protected String concat(int begin) {
		return concat(begin, args.length - 1);
	}
	
	protected String concat(int begin, int end) {
		if(begin > args.length)
			return null;
		if(end > args.length)
			end = args.length - 1;
		StringBuffer sb = new StringBuffer();
		for(int i = begin; i <= end; i++) {
			sb.append(args[i]);
			sb.append(" ");
		}
		return sb.toString().trim();
	}
}
