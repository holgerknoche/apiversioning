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
	public void testSingleNamespaceModelWithPredecessor() {
		var input1 = "namespace test {\n"
				+ " structure Customer {\n"
				+ "  first_name : string\n"
				+ "  last_name : string\n"
				+ " }\n"
				+ "}\n";
		
		var input2 = "from \"previous\"\n" 
				+ "namespace test {\n"
				+ " structure Customer {\n"
				+ "  firstName : string replaces first_name\n"
				+ "  lastName : string replaces last_name\n"
				+ " }\n"
				+ "}\n";
		
		var apiVersion1 = new ApiVersionLoader().loadFromString(input1);
		
		TestApiVersionResolver resolver = TestApiVersionResolver.builder().addVersion("previous", apiVersion1).build();
		var apiVersion2 = new ApiVersionLoader(resolver).loadFromString(input2);
		
		System.out.println(apiVersion2);
	}
	
	@Test
	public void testMissingPredecessor() {
		var input = "from \"nonexisting:1.0\"";
		
		var exception = assertThrows(ApiVersionParseException.class, () -> new ApiVersionLoader(TestApiVersionResolver.builder().build()).loadFromString(input));
		assertEquals("At 1:5: Predecessor model 'nonexisting:1.0' could not be resolved.", exception.getMessage());
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
