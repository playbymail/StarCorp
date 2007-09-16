package starcorp.common.entities;

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
	
}