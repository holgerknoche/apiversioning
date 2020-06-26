package apiversioning.model;

public class FixedStringType extends StringType implements FixedType {

	private final int fixedLength;

	protected FixedStringType(int fixedLength) {
		this.fixedLength = fixedLength;
	}
	
	@Override
	public String toString() {
		return this.getName() + "(" + this.fixedLength + ")";
	}
	
}
