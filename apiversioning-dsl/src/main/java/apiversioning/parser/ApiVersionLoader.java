package apiversioning.parser;

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

import apiversioning.model.ApiVersion;
import apiversioning.parser.ApiVersionParser.ApiVersionSpecificationContext;

public class ApiVersionLoader {
	
	private final ApiVersionResolver versionResolver;
	
	public ApiVersionLoader() {
		this(new IdleApiVersionResolver());
	}
	
	public ApiVersionLoader(ApiVersionResolver versionResolver) {
		this.versionResolver = versionResolver;
	}
	
	public ApiVersion loadFromFile(String fileName) throws IOException {
		return this.loadFromStream(CharStreams.fromFileName(fileName), this.versionResolver);
	}
	
	public ApiVersion loadFromString(String input) {
		return this.loadFromStream(CharStreams.fromString(input), this.versionResolver);
	}
	
	private ApiVersion loadFromStream(CharStream charStream, ApiVersionResolver resolver) {
		ApiVersionLexer lexer = new ApiVersionLexer(charStream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		ApiVersionParser parser = new ApiVersionParser(tokenStream);
				
		ApiVersionSpecificationContext specification = parser.apiVersionSpecification();
		
		// First pass: Find all structural elements to allow forward references
		StructureScanner structureScanner = new StructureScanner();
		specification.accept(structureScanner);
		
		// The actual model is built by the second pass, this time over the object structure
		return new ApiVersionModelBuilder(resolver, structureScanner.getLookup()).buildApiVersionFrom(specification);
	}

}
