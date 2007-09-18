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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import starcorp.common.entities.AColonists;
import starcorp.common.entities.Workers;

/**
 * starcorp.common.types.FacilityType
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public abstract class AFacilityType extends ABaseType {
	protected static final ResourceBundle bundle = ResourceBundle.getBundle("facilities");
	
	private static Map<String, AFacilityType> types = new HashMap<String, AFacilityType>(); 

	static {
		loadTypes();
	}
	
	private static void loadTypes() {
		Enumeration<String> keys = bundle.getKeys();
		while(keys.hasMoreElements()) {
			String key = keys.nextElement();
			if(key.endsWith(".class")) {
				String className = bundle.getString(key);
				AFacilityType type;
				try {
					type = (AFacilityType) Class.forName(className).newInstance();
					type.key = key.substring(0, key.indexOf(".class"));
					types.put(type.getKey(), type);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @param key
	 * @return
	 */
	public static AFacilityType getType(String key) {
		return types.get(key);
	}
	
	public static List<AFacilityType> listTypes(Class<?> typeClass) {
		List<AFacilityType> types = new ArrayList<AFacilityType>();
		Iterator<Map.Entry<String, AFacilityType>> i = AFacilityType.types.entrySet().iterator();
		while(i.hasNext()) {
			Map.Entry<String, AFacilityType> entry = i.next();
			if(entry.getValue().getClass().equals(typeClass)) {
				types.add(entry.getValue());
			}
		}
		return types;
	}
	
	/**
	 * @param type
	 * @param resourceName
	 * @return
	 */
	static String getResource(AFacilityType type, String resourceName) {
		try {
			return bundle.getString(type.getKey() + "." + resourceName);
		}
		catch(MissingResourceException e) {
			return "!" + type.getKey() + "." + resourceName + "!";
		}
	}
	
	private String key;
	
	/**
	 * @return
	 */
	public String getKey() {
		return this.key;
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
	public int getPowerRequirement() {
		return Integer.parseInt(getResource(this, "power"));
	}
	
	public Population getWorkerRequirement(PopulationClass popClass) {
		return getWorkerRequirement().get(popClass);
	}
	
	public Map<PopulationClass, Population> getWorkerRequirement() {
		HashMap<PopulationClass, Population> workers = new HashMap<PopulationClass, Population>();
		Enumeration<String> keys = bundle.getKeys();
		while(keys.hasMoreElements()) {
			String key = keys.nextElement();
			int i;
			if((i = key.indexOf(".worker.type")) != -1) {
				String facilityKey = key.substring(0, i);
				String workerNumber = key.substring(key.lastIndexOf(".") + 1);
				Population colonist = new Population();
				PopulationClass popClass = PopulationClass.getType(bundle.getString(key));
				colonist.setPopClass(popClass);
				colonist.setQuantity(Integer.parseInt(bundle.getString(facilityKey + ".worker.qty." + workerNumber)));
				workers.put(popClass, colonist);
			}
		}
		return workers;
	}
	
	public Set<Items> getBuildingRequirement() {
		HashSet<Items> modules = new HashSet<Items>();
		Enumeration<String> keys = bundle.getKeys();
		while(keys.hasMoreElements()) {
			String key = keys.nextElement();
			int i;
			if((i = key.indexOf(".module.type")) != -1) {
				String facilityKey = key.substring(0, i);
				String moduleNumber = key.substring(key.lastIndexOf(".") + 1);
				Items item = new Items();
				item.setType(bundle.getString(key));
				item.setQuantity(Integer.parseInt(bundle.getString(facilityKey + ".module.qty." + moduleNumber)));
				modules.add(item);
			}
		}
		return modules;
	}
	
	public double getEfficiency(List<Workers> currentWorkers) {
		Map<PopulationClass, Population> requiredWorkers = getWorkerRequirement();
		Iterator<PopulationClass> i = requiredWorkers.keySet().iterator();
		
		int requiredTotal = 0;
		int currentTotal = 0;
		
		while(i.hasNext()) {
			PopulationClass popClass = i.next();
			Population c = requiredWorkers.get(popClass);
			requiredTotal += c.getQuantity();
		}
		
		Iterator<Workers> n = currentWorkers.iterator();
		
		while(n.hasNext()) {
			AColonists w = n.next();
			Population c1 = w.getPopulation();
			Population c2 = requiredWorkers.get(c1.getPopClass());
			if(c2 != null) {
				if(c1.getQuantity() <= c2.getQuantity()) {
					currentTotal += c1.getQuantity();
				}
				else {
					currentTotal += c2.getQuantity();
				}
			}
		}
		
		return ((double) requiredTotal / (double) currentTotal); 
	}
}
