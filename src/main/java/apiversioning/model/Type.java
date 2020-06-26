package apiversioning.model;

public interface Type {
	
	public String getName();

	public String getFullyQualifiedName();
	
	public boolean isFixed();
	
	public boolean isBaseType();
	
	public boolean isStructure();
	
	public boolean isEnumeration();
	
}
