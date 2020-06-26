package apiversioning.model;

public abstract class UserDefinedType implements Type {

	private final Namespace namespace;

	private final String name;
	
	public UserDefinedType(Namespace namespace, String name) {
		this.namespace = namespace;
		this.name = name;
	}
	
	public Namespace namespace() {
		return this.namespace;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getFullyQualifiedName() {
		return this.namespace.getName() + "." + this.getName();
	}
	
	@Override
	public final boolean isBaseType() {
		return false;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
}
