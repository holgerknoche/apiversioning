package apiversioning.model;

public abstract class StringType extends BaseType {
		
	public static StringType ofDynamicLength() {
		return DynamicStringType.INSTANCE;
	}
	
	public static StringType ofFixedLength(int fixedLength) {
		return new FixedStringType(fixedLength);
	}
		
	@Override
	public String getName() {
		return "string";
	}		

}
