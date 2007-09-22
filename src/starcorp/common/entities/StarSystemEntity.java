package starcorp.common.entities;

import org.dom4j.Element;

import starcorp.common.types.CoordinatesPolar;

public class StarSystemEntity extends ANamedEntity {

	protected StarSystem system;
	protected CoordinatesPolar location;
	protected boolean asteroid;
	protected boolean gasfield;
	
	public StarSystemEntity() {
		super();
	}

	public StarSystem getSystem() {
		return system;
	}
	public void setSystem(StarSystem system) {
		this.system = system;
	}
	public CoordinatesPolar getLocation() {
		return location;
	}
	public void setLocation(CoordinatesPolar location) {
		this.location = location;
	}

	@Override
	public void readXML(Element e) {
		super.readXML(e);
		this.system = new StarSystem();
		this.system.readXML(e.element("system").element("entity"));
		this.location = new CoordinatesPolar(e);
	}

	@Override
	public Element toBasicXML(Element parent) {
		Element e =super.toBasicXML(parent);
		system.toBasicXML(parent.addElement("system"));
		location.toXML(e);
		return e;
	}

	@Override
	public String toString() {
		return super.toString() + " @ " + system.getName() + " [" + system.getID() + "] " + location;
	}

	public boolean isAsteroid() {
		return asteroid;
	}

	public void setAsteroid(boolean asteroid) {
		this.asteroid = asteroid;
	}

	public boolean isGasfield() {
		return gasfield;
	}

	public void setGasfield(boolean gasfield) {
		this.gasfield = gasfield;
	}
	
}