package apiversioning.model;

public class DynamicStringType extends StringType {
	
	protected static final DynamicStringType INSTANCE = new DynamicStringType();
	
	private DynamicStringType() {
		// Empty constructor
	}
	
	@Override
	public boolean isFixed() {
		return false;
	}

}
