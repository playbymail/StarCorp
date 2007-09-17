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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * starcorp.common.types.TerrainType
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public class TerrainType extends ABaseType {
	private static final ResourceBundle bundle = ResourceBundle.getBundle("terrain");
	
	private static Map<String, TerrainType> types = new HashMap<String, TerrainType>(); 

	static {
		loadTypes();
	}
	
	private static void loadTypes() {
		Enumeration<String> keys = bundle.getKeys();
		while(keys.hasMoreElements()) {
			String key = keys.nextElement();
			if(key.endsWith(".key")) {
				String k = bundle.getString(key);
				TerrainType type = new TerrainType(k);
				types.put(k, type);
			}
		}
	}
	
	/**
	 * @param key
	 * @return
	 */
	public static TerrainType getType(String key) {
		return types.get(key);
	}
	
	/**
	 * @param type
	 * @param resourceName
	 * @return
	 */
	static String getResource(TerrainType type, String resourceName) {
		try {
			return bundle.getString(type.getKey() + "." + resourceName);
		}
		catch(MissingResourceException e) {
			return "!" + type.getKey() + "." + resourceName + "!";
		}
	}
	
	private String key;
	
	public TerrainType(String key) {
		this.key = key;
	}
	
	/**
	 * @return
	 */
	public String getKey() {
		return key;
	}
	
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