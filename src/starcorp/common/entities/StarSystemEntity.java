package starcorp.common.entities;

import org.dom4j.Element;

import starcorp.common.types.CoordinatesPolar;

public class StarSystemEntity extends ANamedEntity {

	protected long systemID;
	protected CoordinatesPolar location;
	protected boolean asteroid;
	protected boolean gasfield;
	
	public StarSystemEntity() {
		super();
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
		try {
			this.systemID = Long.parseLong(e.attributeValue("system"));
		}
		catch(NumberFormatException ex) {
			// ignore
		}
		this.location = new CoordinatesPolar(e);
	}

	@Override
	public Element toBasicXML(Element parent) {
		Element e =super.toBasicXML(parent);
		e.addAttribute("system", String.valueOf(systemID));
		location.toXML(e);
		return e;
	}

	@Override
	public String toString() {
		return super.toString() + " @ system " + systemID + " " + location;
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

	public long getSystemID() {
		return systemID;
	}

	public void setSystemID(long systemID) {
		this.systemID = systemID;
	}
	
}