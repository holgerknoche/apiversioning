package apiversioning.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Structure extends UserDefinedType {
	
	private final boolean fixed;
	
	private final List<Field> fields = new ArrayList<>();
	
	private final Map<String, Field> fieldLookup = new HashMap<>();
	
	private Optional<Structure> predecessor = Optional.empty();
	
	public Structure(Namespace namespace, String name, boolean fixed) {
		super(namespace, name);
		
		this.fixed = fixed;
	}
	
	public Optional<Structure> getPredecessor() {
		return predecessor;
	}
	
	public void setPredecessor(Optional<Structure> predecessor) {
		this.predecessor = predecessor;
	}
	
	@Override
	public boolean isStructure() {
		return true;
	}
	
	@Override
	public boolean isEnumeration() {
		return false;
	}
	
	public boolean isFixed() {
		return this.fixed;
	}
	
	public void addField(Field field) {
		this.fields.add(field);
		this.fieldLookup.put(field.getName(), field);
	}
	
	@Override
	public Optional<Field> findField(String name) {
		return Optional.ofNullable(this.fieldLookup.get(name));
	}
		
}
