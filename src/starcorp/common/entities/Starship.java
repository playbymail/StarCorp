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

import org.dom4j.Element;

import starcorp.common.types.AItemType;
import starcorp.common.types.BuildingModules;
import starcorp.common.types.ConsumerGoods;
import starcorp.common.types.Coordinates2D;
import starcorp.common.types.GalacticDate;
import starcorp.common.types.IndustrialGoods;
import starcorp.common.types.Items;
import starcorp.common.types.Resources;
import starcorp.common.types.StarshipHulls;

/**
 * starcorp.common.entities.Starship
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 15 Sep 2007
 */
public class Starship extends StarSystemEntity {
	public static final int TIME_UNITS_PER_TURN = 100;
	
	private Corporation owner;
	private Planet planet;
	private Coordinates2D planetLocation;
	private Colony colony;
	private StarshipDesign design;
	private Set<Items> cargo = new HashSet<Items>();
	private GalacticDate builtDate;
	private int timeUnitsUsed;
	
	public boolean inOpenSpace() {
		return !isDocked() && !inOrbit();
	}
	
	public boolean isDocked() {
		return getPlanetLocation() != null || getColony() != null;
	}
	
	public boolean inOrbit() {
		return getPlanetLocation() == null && getColony() == null && getPlanet() != null;
	}
	
	public boolean isOrbiting(Planet planet) {
		return getPlanetLocation() == null && getColony() == null && planet.equals(getPlanet());
	}
	
	public boolean isOrbiting(long planetID) {
		return getPlanetLocation() == null && getColony() == null && getPlanet() != null && getPlanet().getID() == planetID;
	}

	public String getLocationDescription() {
		StringBuffer loc = new StringBuffer();
		
		if(getColony() != null) {
			loc.append("Docked at ");
			loc.append(getColony().getName());
			loc.append(" [");
			loc.append(getColony().getID());
			loc.append("] on ");
			loc.append(getPlanet().getName());
			loc.append(" [");
			loc.append(getPlanet().getID());
			loc.append("]");
		}
		else if(getPlanet() != null) {
			if(getPlanetLocation() != null) {
				loc.append("Docked at");
				loc.append(getPlanetLocation());
				loc.append(" on ");
				loc.append(getPlanet().getName());
				loc.append(" [");
				loc.append(getPlanet().getID());
				loc.append("]");
			}
			else {
				loc.append("Orbiting ");
				loc.append(getPlanet().getName());
				loc.append(" [");
				loc.append(getPlanet().getID());
				loc.append("]");
			}
		}
		else {
			loc.append("At ");
			loc.append(getLocation());
			loc.append(" in system ");
			loc.append(getSystemID());
		}
		
		return loc.toString();
	}
	
	public int getCargoConsumerGoodsMass() {
		int mass = 0;
		Iterator<Items> i = cargo.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			if(item.getTypeClass() instanceof ConsumerGoods) {
				mass += (item.getTypeClass().getMassUnits() * item.getQuantity());
			}
		}
		return mass;
	}
	
	public int getCargoIndustrialGoodsMass() {
		int mass = 0;
		Iterator<Items> i = cargo.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			if(item.getTypeClass() instanceof IndustrialGoods) {
				mass += (item.getTypeClass().getMassUnits() * item.getQuantity());
			}
			else if(item.getTypeClass() instanceof Resources) {
				Resources r = (Resources) item.getTypeClass();
				if(r.isFissile() || r.isMetal() || r.isMineral()) {
					mass += (r.getMassUnits() * item.getQuantity()); 
				}
			}
		}
		return mass;
	}
	
	public int getCargoModulesMass() {
		int mass = 0;
		Iterator<Items> i = cargo.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			if(item.getTypeClass() instanceof BuildingModules || item.getTypeClass() instanceof StarshipHulls) {
				mass += (item.getTypeClass().getMassUnits() * item.getQuantity());
			}
		}
		return mass;
		
	}
	
	public int getCargoOrganicsMass() {
		int mass = 0;
		Iterator<Items> i = cargo.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			if(item.getTypeClass() instanceof Resources) {
				Resources r = (Resources) item.getTypeClass();
				if(r.isOrganic()) {
					mass += (r.getMassUnits() * item.getQuantity()); 
				}
			}
		}
		return mass;
		
	}
	
	public int getCargoLiquidGasMass() {
		int mass = 0;
		Iterator<Items> i = cargo.iterator();
		while(i.hasNext()) {
			Items item = i.next();
			if(item.getTypeClass() instanceof Resources) {
				Resources r = (Resources) item.getTypeClass();
				if(r.isLiquid() || r.isGas()) {
					mass += (r.getMassUnits() * item.getQuantity()); 
				}
			}
		}
		return mass;
		
	}
	
	public int getSpaceFor(AItemType type) {
		int mu = getCargoSpace(type);
		return mu / type.getMassUnits();
	}
	
	public int getCargoSpace(AItemType type) {
		int cargoSpace = 0;
		if(type instanceof ConsumerGoods) {
			cargoSpace = design.getConsumerCapacity() - getCargoConsumerGoodsMass();
		}
		else if(type instanceof IndustrialGoods) {
			cargoSpace = design.getIndustrialCapacity() - getCargoIndustrialGoodsMass();
		}
		else if(type instanceof BuildingModules || type instanceof StarshipHulls) {
			cargoSpace = design.getModulesCapacity() - getCargoModulesMass();
		}
		else if(type instanceof Resources) {
			Resources r = (Resources) type;
			if(r.isFissile() || r.isMetal() || r.isMineral()) {
				cargoSpace = design.getIndustrialCapacity() - getCargoIndustrialGoodsMass();
			}
			else if(r.isGas() || r.isLiquid()) {
				cargoSpace = design.getLiquidGasCapacity() - getCargoLiquidGasMass();
			}
			else if(r.isOrganic()) {
				cargoSpace = design.getOrganicsCapacity() - getCargoOrganicsMass();
			}
		}
		return cargoSpace;
	}
	
	public Items getCargo(AItemType type) {
		for(Items item : cargo) {
			if(item.getTypeClass().equals(type)) {
				return item;
			}
		}
		return null;
	}
	
	private Items setCargo(Items item) {
		Iterator<Items> i = cargo.iterator();
		while(i.hasNext()) {
			Items item2 = i.next();
			if(item2.getTypeClass().equals(item.getTypeClass())) {
				i.remove();
			}
			else if(item2.getQuantity() < 1)
				i.remove();
		}
		cargo.add(item);
		return item;
	}
	
	public int addCargo(AItemType type, int quantity) {
		Items item = getCargo(type);
		int qty = quantity;
		int spaceFor = getSpaceFor(type);
		if(spaceFor < qty) {
			qty = spaceFor;
		}
		if(item == null) {
			item = new Items();
			item.setQuantity(qty);
			item.setTypeClass(type);
		}
		else {
			item.add(qty);
		}
		if(item.getQuantity() > 0)
			setCargo(item);
		return qty;
	}
	
	public int removeCargo(AItemType type, int quantity) {
		Items item = getCargo(type);
		if(item == null) {
			return 0;
		}
		int qty = quantity > item.getQuantity() ? item.getQuantity() : quantity;
		item.remove(qty);
		setCargo(item);
		return qty;
	}
	
	public Corporation getOwner() {
		return owner;
	}
	public void setOwner(Corporation owner) {
		this.owner = owner;
	}
	public Planet getPlanet() {
		return planet;
	}
	public void setPlanet(Planet planet) {
		this.planet = planet;
	}
	public Coordinates2D getPlanetLocation() {
		return planetLocation;
	}
	public void setPlanetLocation(Coordinates2D planetLocation) {
		this.planetLocation = planetLocation;
	}
	public StarshipDesign getDesign() {
		return design;
	}
	public void setDesign(StarshipDesign design) {
		this.design = design;
	}
	public Set<Items> getCargo() {
		return cargo;
	}
	public void setCargo(Set<Items> cargo) {
		this.cargo = cargo;
	}
	public GalacticDate getBuiltDate() {
		return builtDate;
	}
	public void setBuiltDate(GalacticDate builtDate) {
		this.builtDate = builtDate;
	}
	
	public Colony getColony() {
		return colony;
	}

	public void setColony(Colony colony) {
		this.colony = colony;
	}

	public int getTimeUnitsUsed() {
		return timeUnitsUsed;
	}

	public void setTimeUnitsUsed(int timeUnitsUsed) {
		this.timeUnitsUsed = timeUnitsUsed;
	}
	
	public int getTimeUnitsRemaining() {
		return TIME_UNITS_PER_TURN - timeUnitsUsed;
	}
	
	public void setTimeUnitsRemaining(int remaining) {
		this.timeUnitsUsed = (TIME_UNITS_PER_TURN - remaining);
	}
	
	public void incrementTimeUnitsUsed(int used) {
		this.timeUnitsUsed += used;
	}
	
	public boolean enoughTimeUnits(int required) {
		return getTimeUnitsRemaining() >= required;
	}

	@Override
	public void readXML(Element e) {
		super.readXML(e);
		this.owner = new Corporation();
		this.owner.readXML(e.element("owner").element("entity"));
		String sTU = e.attributeValue("TU");
		if(sTU != null) {
			timeUnitsUsed = Integer.parseInt(sTU);
		}
		Element ePlanet = e.element("planet");
		if(ePlanet != null) {
			this.planet = new Planet();
			this.planet.readXML(ePlanet.element("entity"));
		}
		Element eLoc = e.element("planet-location");
		if(eLoc != null) {
			this.planetLocation = new Coordinates2D(eLoc);
		}
		Element eCol = e.element("colony");
		if(eCol != null) {
			this.colony = new Colony();
			this.colony.readXML(eCol.element("entity"));
		}
		this.design = new StarshipDesign();
		this.design.readXML(e.element("design").element("entity"));
		this.builtDate = new GalacticDate(e.element("built").element("date"));
		
		Element eCargo = e.element("cargo");
		if(eCargo != null) {
			for(Iterator i = eCargo.elementIterator("item"); i.hasNext();) {
				cargo.add(new Items((Element)i.next()));
			}
		}
	}

	@Override
	public Element toBasicXML(Element parent) {
		Element e = super.toBasicXML(parent);
		owner.toBasicXML(e.addElement("owner"));
		if(planet != null)
			planet.toBasicXML(e.addElement("planet"));
		if(planetLocation != null)
			planetLocation.toXML(e.addElement("planet-location"));
		if(colony != null)
			colony.toBasicXML(e.addElement("colony"));
		design.toBasicXML(e.addElement("design"));
		builtDate.toXML(e.addElement("built"));
		return e;
	}

	@Override
	public Element toFullXML(Element parent) {
		Element e =super.toFullXML(parent);
		e.addAttribute("TU", String.valueOf(timeUnitsUsed));
		Element eCargo = e.addElement("cargo");
		Iterator<Items> i = cargo.iterator();
		while(i.hasNext()) {
			i.next().toXML(eCargo.addElement("item"));
		}
		return e;
	}

	@Override
	public String toString() {
		return super.toString() + " [" + design.getName() + "}" + (planet == null ? "" : " " + planet.getName() + " (" + planet.getID() + ")") + (planetLocation == null ? "" : " " + planetLocation) + (colony == null ? "" : " " + colony.getName() + " (" + colony.getID() + ")");
	} 

}
