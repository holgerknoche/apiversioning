package apiversioning.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.junit.jupiter.api.Test;

import apiversioning.parser.ApiVersionParser.FromStatementContext;
import apiversioning.parser.ApiVersionParser.NamespaceDeclarationContext;

public class ApiVersionParserTest {

	private <T> T parse(String input, ParserRule<T> rule) {
		CharStream charStream = CharStreams.fromString(input);
		ApiVersionLexer lexer = new ApiVersionLexer(charStream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		ApiVersionParser parser = new ApiVersionParser(tokenStream);
		
		parser.setErrorHandler(new BailErrorStrategy());
		
		return rule.apply(parser);
	}
	
	@Test
	public void testFromStatement() {
		String input = "from \"model_v1\"\n";
		
		FromStatementContext fromStatement = this.parse(input, ApiVersionParser::fromStatement);
		
		assertEquals("\"model_v1\"", fromStatement.previousVersion.getText());
	}
	
	@Test
	public void testEmptyNamespaceDeclaration() {
		String input = "namespace test.apiversioning {}";
		
		NamespaceDeclarationContext packageStatement = this.parse(input, ApiVersionParser::namespaceDeclaration);		
	}
	
	@Test
	public void testPackageStatement() {
		
	}
	
	@FunctionalInterface
	private interface ParserRule<T> extends Function<ApiVersionParser, T> {}
	
}
