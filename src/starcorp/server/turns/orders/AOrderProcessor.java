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
package starcorp.server.turns.orders;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import starcorp.client.turns.TurnError;
import starcorp.client.turns.TurnOrder;
import starcorp.server.entitystore.IEntityStore;

/**
 * starcorp.server.turns.AOrderProcessor
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public abstract class AOrderProcessor {

	private static ResourceBundle bundle = ResourceBundle.getBundle("orderprocessors");
	
	private static Map<String, AOrderProcessor> processors = new HashMap<String, AOrderProcessor>();
	
	static {
		loadProcessors();
	}
	
	private static void loadProcessors() {
		Enumeration<String> keys = bundle.getKeys();
		while(keys.hasMoreElements()) {
			String key = keys.nextElement();
			if(key.endsWith(".class")) {
				String className = bundle.getString(key);
				AOrderProcessor processor;
				try {
					processor = (AOrderProcessor) Class.forName(className).newInstance();
					processor.key = key.substring(0, key.indexOf(".class"));
					processors.put(processor.getKey(), processor);
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static AOrderProcessor getProcessor(String type) {
		return processors.get(type);
	}

	private String key;
	protected IEntityStore entityStore;
	
	public abstract TurnError process(TurnOrder order);

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public IEntityStore getEntityStore() {
		return entityStore;
	}

	public void setEntityStore(IEntityStore entityStore) {
		this.entityStore = entityStore;
	}
}
