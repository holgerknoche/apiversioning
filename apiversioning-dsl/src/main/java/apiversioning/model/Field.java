package apiversioning.model;

import java.util.Optional;

public class Field {

	private final Structure owner;
	
	private final String name;
	
	private final Type type;
	
	private Optional<Field> predecessor = Optional.empty();

	public Field(Structure owner, String name, Type type) {
		this.owner = owner;
		this.name = name;
		this.type = type;
	}
	
	public Optional<Field> getPredecessor() {
		return this.predecessor;
	}
	
	public void setPredecessor(Optional<Field> predecessor) {
		this.predecessor = predecessor;
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
