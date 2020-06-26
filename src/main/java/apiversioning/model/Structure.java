package apiversioning.model;

import java.util.ArrayList;
import java.util.List;

public class Structure extends UserDefinedType {
	
	private final boolean fixed;
	
	private final List<Field> fields = new ArrayList<>();
	
	public Structure(Namespace namespace, String name, boolean fixed) {
		super(namespace, name);
		
		this.fixed = fixed;
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
	}
		
}
