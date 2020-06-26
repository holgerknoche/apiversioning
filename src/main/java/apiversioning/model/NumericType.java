package apiversioning.model;

public class NumericType extends BaseType implements FixedType {
	
	private final int integerPlaces;
	
	private final int fractionalPlaces;

	public NumericType(int integerPlaces, int fractionalPlaces) {
		this.integerPlaces = integerPlaces;
		this.fractionalPlaces = fractionalPlaces;
	}

	@Override
	public String getName() {
		return "numeric";
	}
	
	@Override
	public String toString() {
		return this.getName() + "(" + this.integerPlaces + "," + this.fractionalPlaces + ")";
	}

}
