package apiversioning.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import apiversioning.parser.ApiVersionLoader;
import apiversioning.parser.ApiVersionParseException;

public class ApiVersionModelBuildingTest {
	
	@Test
	public void testSingleNamespaceModelWithoutPredecessor() {
		var input = "namespace test {\n"
				+ " structure Customer {\n"
				+ "  firstName : string\n"
				+ "  lastName : string\n"
				+ "  age : int32\n"
				+ "  address : Address\n"
				+ " }\n"
				+ "\n"
				+ " structure Address {\n"
				+ "  zipCode : numeric(5)\n"
				+ "  cityName : string\n"
				+ " }\n"
				+ "}";

		var apiVersion = new ApiVersionLoader().loadFromString(input);
		System.out.println(apiVersion);
	}
	
	@Test
	public void testUnfixedFieldInFixedStruct() {
		var input = "namespace test {\n"
				+ " fixed structure Test {\n"
				+ "  fixedField : string(20)\n"
				+ "  dynamicField : string\n"
				+ " }\n"
				+ "}";

		var exception = assertThrows(ApiVersionParseException.class, () -> new ApiVersionLoader().loadFromString(input));		
		assertEquals("At 4:2: Dynamic field 'dynamicField' in fixed structure 'test.Test'.", exception.getMessage());
	}

}
