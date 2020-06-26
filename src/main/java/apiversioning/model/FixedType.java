package apiversioning.model;

public interface FixedType extends Type {
	
	default public boolean isFixed() {
		return true;
	}

}
