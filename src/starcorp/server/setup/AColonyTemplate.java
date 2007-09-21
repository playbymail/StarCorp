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
package starcorp.server.setup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import starcorp.common.entities.Colony;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.Planet;
import starcorp.common.types.Coordinates2D;
import starcorp.common.util.PackageExplorer;
import starcorp.server.entitystore.IEntityStore;

/**
 * starcorp.server.setup.AColonyTemplate
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 20 Sep 2007
 */
public abstract class AColonyTemplate {

	private static Map<String, AColonyTemplate> templates = new HashMap<String, AColonyTemplate>();
	
	static {
		List<Class> classes;
		try {
			classes = PackageExplorer.getClassesForPackage("starcorp.server.setup.colonies");
			for(Class clazz : classes) {
				AColonyTemplate template = (AColonyTemplate) clazz.newInstance();
				templates.put(clazz.getSimpleName(), template);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static AColonyTemplate getTemplate(String name) {
		return templates.get(name);
	}
	
	protected IEntityStore entityStore;
	
	public void setEntityStore(IEntityStore entityStore) {
		this.entityStore = entityStore;
	}
	
	public Colony create(int govtID, int planetID, int x, int y, String name) {
		Corporation govt = (Corporation) entityStore.load(Corporation.class, govtID);
		Planet planet = (Planet) entityStore.load(Planet.class, planetID);
		if(govt == null || planet == null) {
			return null;
		}
		Coordinates2D location = new Coordinates2D(x,y);
		return create(govt,planet,location,name);
	}
	
	public abstract Colony create(Corporation govt, Planet planet, Coordinates2D location, String name);
}
