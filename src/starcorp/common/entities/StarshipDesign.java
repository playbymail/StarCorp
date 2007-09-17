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
package starcorp.common.entities;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import starcorp.common.types.GalacticDate;
import starcorp.common.types.Items;
import starcorp.common.types.StarshipHull;

/**
 * starcorp.common.entities.StarshipDesign
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public class StarshipDesign extends ANamedEntity {

	private Corporation owner;
	private String name;
	private GalacticDate designDate;
	private Set<Items> hulls = new HashSet<Items>();
	
	public boolean isValid() {
		int total = countHulls();
		int command = countCommandHulls();
		int crew = countCrewHulls();
		
		if(command < 1)
			return false;
		
		int nonCrewOrCommand = total - (command + crew);
		
		return (nonCrewOrCommand / 3) <= crew;
	}
	
	public int countHulls() {
		int total = 0;
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			total += item.getQuantity();
		}
		return total;
	}
	
	public int countHulls(StarshipHull type) {
		int total = 0;
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			if(type.equals(item.getTypeClass())) {
				total += item.getQuantity();
			}
		}
		return total;
	}
	
	public int countCommandHulls() {
		int total = 0;
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			StarshipHull hull = (StarshipHull) item.getTypeClass();
			if(hull.isCommand()) {
				total += item.getQuantity();
			}
		}
		return total;
	}

	public int countCrewHulls() {
		int total = 0;
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			StarshipHull hull = (StarshipHull) item.getTypeClass();
			if(hull.isCrew()) {
				total += item.getQuantity();
			}
		}
		return total;
	}

	public int getConsumerCapacity() {
		int total = 0;
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			StarshipHull hull = (StarshipHull) item.getTypeClass();
			total += item.getQuantity() * hull.getConsumerCapacity();
		}
		return total;
	}

	public int getIndustrialCapacity() {
		int total = 0;
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			StarshipHull hull = (StarshipHull) item.getTypeClass();
			total += item.getQuantity() * hull.getIndustrialCapacity();
		}
		return total;
	}

	public int getModulesCapacity() {
		int total = 0;
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			StarshipHull hull = (StarshipHull) item.getTypeClass();
			total += item.getQuantity() * hull.getModulesCapacity();
		}
		return total;
	}

	public int getOrganicsCapacity() {
		int total = 0;
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			StarshipHull hull = (StarshipHull) item.getTypeClass();
			total += item.getQuantity() * hull.getOrganicsCapacity();
		}
		return total;
	}

	public int getLiquidGasCapacity() {
		int total = 0;
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			StarshipHull hull = (StarshipHull) item.getTypeClass();
			total += item.getQuantity() * hull.getLiquidGasCapacity();
		}
		return total;
	}
	
	public int getTotalMass() {
		int total = 0;
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			StarshipHull hull = (StarshipHull) item.getTypeClass();
			total += item.getQuantity() * hull.getMassUnits();
		}
		return total;
	}

	public int getJumpRange() {
		int maxWarp = 0;
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			StarshipHull hull = (StarshipHull) item.getTypeClass();
			if(hull.getWarpFactor() > maxWarp)
				maxWarp = hull.getWarpFactor();
		}
		return maxWarp;
	}

	public double getImpulseSpeed() {
		int totalThrust = 0;
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			StarshipHull hull = (StarshipHull) item.getTypeClass();
			totalThrust += item.getQuantity() * hull.getThrustPower();
		}
		int totalMass = getTotalMass();
		return ((double) totalMass / (double) totalThrust);
	}

	public double getMaxOrbitGravity() {
		return 5 / getImpulseSpeed();
	}
	
	public double getMaxDockGravity() {
		return 3 / getImpulseSpeed();
	}
	
	public boolean canMineAsteroid() {
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			StarshipHull hull = (StarshipHull) item.getTypeClass();
			if(hull.isAsteroidMining())
				return true;
		}
		return false;
	}

	public boolean canMineGasField() {
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			StarshipHull hull = (StarshipHull) item.getTypeClass();
			if(hull.isGasFieldMining())
				return true;
		}
		return false;
	}

	public int getScanGalaxyRange() {
		int range = 0;
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			StarshipHull hull = (StarshipHull) item.getTypeClass();
			range += hull.getLongScanner();
			
		}
		return range;
	}

	public boolean canScanStarSystem() {
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			StarshipHull hull = (StarshipHull) item.getTypeClass();
			if(hull.isShortScanner())
				return true;
		}
		return false;
	}

	public boolean canProbeField() {
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			StarshipHull hull = (StarshipHull) item.getTypeClass();
			if(hull.isSystemProbe())
				return true;
		}
		return false;
	}

	public boolean canProbePlanet() {
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			StarshipHull hull = (StarshipHull) item.getTypeClass();
			if(hull.isPlanetProbe())
				return true;
		}
		return false;
	}

	public boolean hasBioLab() {
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			StarshipHull hull = (StarshipHull) item.getTypeClass();
			if(hull.isBioLab())
				return true;
		}
		return false;
	}

	public boolean hasPhysicsLab() {
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			StarshipHull hull = (StarshipHull) item.getTypeClass();
			if(hull.isPhysicsLab())
				return true;
		}
		return false;
	}

	public boolean hasGeoLab() {
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			StarshipHull hull = (StarshipHull) item.getTypeClass();
			if(hull.isGeoLab())
				return true;
		}
		return false;
	}

	public Corporation getOwner() {
		return owner;
	}
	public void setOwner(Corporation owner) {
		this.owner = owner;
	}
	
	public Items getHulls(StarshipHull hullType) {
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			if(item.getTypeClass().equals(hullType)) {
				return item;
			}
		}
		return null;
	}
	
	public Items setHulls(Items item) {
		Iterator<Items> i = hulls.iterator();
		while(i.hasNext()) {
			Items item2 = i.next();
			if(item2.getTypeClass().equals(item.getTypeClass())) {
				item2.add(item.getQuantity());
				item = item2;
				i.remove();
			}
		}
		hulls.add(item);
		return item;
	}
	
	public void addHulls(StarshipHull hullType, int quantity) {
		Items item = getHulls(hullType);
		if(item == null) {
			item = new Items();
			item.setQuantity(quantity);
			item.setTypeClass(hullType);
		}
		else {
			item.add(quantity);
		}
		setHulls(item);
	}
	
	public Set<Items> getHulls() {
		return hulls;
	}
	public void setHulls(Set<Items> hulls) {
		this.hulls = hulls;
	}
	public GalacticDate getDesignDate() {
		return designDate;
	}
	public void setDesignDate(GalacticDate designDate) {
		this.designDate = designDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
