package apiversioning.parser;

import java.util.Iterator;

import apiversioning.parser.ApiVersionParser.IdentifierContext;
import apiversioning.parser.ApiVersionParser.QualifiedNameContext;

class ParseUtil {

	public static String asText(QualifiedNameContext qualifiedName) {
		StringBuilder nameBuilder = new StringBuilder();		
		
		Iterator<IdentifierContext> parts = qualifiedName.parts.iterator();
		while (parts.hasNext()) {
			IdentifierContext part = parts.next();
			
			nameBuilder.append(asText(part));
			
			if (parts.hasNext()) {
				nameBuilder.append('.');
			}
		}
		
		return nameBuilder.toString();
	}
	
	public static String asText(IdentifierContext identifier) {
		if (identifier.ID() != null) {
			return identifier.ID().getText();
		} else {
			return unquote(identifier.LITERAL_ID().getText());
		}
	}
	
	public static String unquote(String input) {
		int lastIndex = (input.length() - 1);
		
		if (lastIndex >= 1 && (input.charAt(0) == input.charAt(lastIndex))) {
			return input.substring(1, lastIndex);
		} else {
			return input;
		}
	}
	
}
