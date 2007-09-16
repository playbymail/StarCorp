package starcorp.common.entities;

public abstract class ANamedEntity extends ABaseEntity {

	protected String name;

	public ANamedEntity() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}