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
package starcorp.common.types;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import starcorp.client.turns.OrderReport;
import starcorp.common.entities.ABaseEntity;

/**
 * starcorp.common.types.EventType
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public abstract class AEventType extends ABaseType {
	private static final ResourceBundle bundle = ResourceBundle.getBundle("events");
	
	private static Map<String, AEventType> types = new HashMap<String, AEventType>(); 

	public abstract OrderReport trigger(ABaseEntity entity);
	
	/**
	 * @param type
	 */
	static void registerType(AEventType type) {
		types.put(type.getKey(), type);
	}
	
	/**
	 * @param key
	 * @return
	 */
	public static AEventType getType(String key) {
		return types.get(key);
	}
	
	/**
	 * @param type
	 * @param resourceName
	 * @return
	 */
	static String getResource(AEventType type, String resourceName) {
		try {
			return bundle.getString(type.getKey() + "." + resourceName);
		}
		catch(MissingResourceException e) {
			return "!" + type.getKey() + "." + resourceName + "!";
		}
	}
	
	/**
	 * @return
	 */
	public abstract String getKey();
	
	/**
	 * @return
	 */
	public String getName() {
		return getResource(this, "name");
	}
	

}
