package starcorp.common.entities;

import org.dom4j.Element;

import starcorp.common.types.CoordinatesPolar;

public abstract class AStarSystemEntity extends ANamedEntity {

	protected StarSystem system;
	protected CoordinatesPolar location;

	public AStarSystemEntity() {
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
		return super.toString() + " @ " + system + " " + location;
	}
	
}