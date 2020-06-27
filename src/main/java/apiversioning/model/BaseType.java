package apiversioning.model;

import java.util.Optional;

public abstract class BaseType implements Type {

	@Override
	public final boolean isBaseType() {
		return true;
	} 
	
	@Override
	public final boolean isStructure() {
		return false;
	}
	
	@Override
	public final boolean isEnumeration() {
		return false;
	}
	
	@Override
	public String getFullyQualifiedName() {
		return this.getName();
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
	@Override
	public Optional<Field> findField(String name) {
		return Optional.empty();
	}
	
}
