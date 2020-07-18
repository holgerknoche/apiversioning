package apiversioning.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum PrimitiveType implements FixedType {
	INT32("int32"),
	INT64("int64"),
	FLOAT32("float32"),
	FLOAT64("float64");
	
	private static final Map<String, PrimitiveType> LITERAL_LOOKUP = createLiteralLookup();
	
	private static Map<String, PrimitiveType> createLiteralLookup() {
		var lookup = new HashMap<String, PrimitiveType>();
		
		for (PrimitiveType type : values()) {
			lookup.put(type.literal, type);
		}
		
		return lookup;
	}
	
	public static PrimitiveType forLiteral(String literal) {
		return LITERAL_LOOKUP.get(literal);
	}
	
	private final String literal;
	
	private PrimitiveType(String literal) {
		this.literal = literal;
	}
	
	@Override
	public boolean isBaseType() {
		return true;
	}
	
	@Override
	public boolean isStructure() {
		return false;
	}
	
	@Override
	public boolean isEnumeration() {
		return false;
	}
		
	public String getName() {
		return this.literal;
	}
	
	@Override
	public String getFullyQualifiedName() {
		return this.name();
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
