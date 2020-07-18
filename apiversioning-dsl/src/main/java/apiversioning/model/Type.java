package apiversioning.model;

import java.util.Optional;

public interface Type {
	
	public String getName();

	public String getFullyQualifiedName();
	
	public boolean isFixed();
	
	public boolean isBaseType();
	
	public boolean isStructure();
	
	public boolean isEnumeration();
	
	public Optional<Field> findField(String name);
	
}
