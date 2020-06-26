package apiversioning.model;

public class Field {

	private final Structure owner;
	
	private final String name;
	
	private final Type type;

	public Field(Structure owner, String name, Type type) {
		this.owner = owner;
		this.name = name;
		this.type = type;
	}
	
	public Structure getOwner() {
		return this.owner;
	}
	
	public String getName() {
		return name;
	}
	
	public Type getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return this.getName() + ":" + this.getType();
	}
	
}
