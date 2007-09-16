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

/**
 * starcorp.common.types.AtmosphereType
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public abstract class AtmosphereType extends ABaseType {
	private static final ResourceBundle bundle = ResourceBundle.getBundle("items");
	
	private static Map<String, AtmosphereType> types = new HashMap<String, AtmosphereType>(); 

	/**
	 * @param type
	 */
	static void registerType(AtmosphereType type) {
		types.put(type.getKey(), type);
	}
	
	/**
	 * @param key
	 * @return
	 */
	public static AtmosphereType getType(String key) {
		return types.get(key);
	}
	
	/**
	 * @param type
	 * @param resourceName
	 * @return
	 */
	static String getResource(AtmosphereType type, String resourceName) {
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
	
	/**
	 * @return
	 */
	public double getHazardLevel() {
		return Double.parseDouble(getResource(this,"hazard"));
	}
	
	
	
	
}
