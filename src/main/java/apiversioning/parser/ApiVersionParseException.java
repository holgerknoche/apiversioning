package apiversioning.parser;

import org.antlr.v4.runtime.Token;

public class ApiVersionParseException extends RuntimeException {		
	
	private static final long serialVersionUID = -4202105095104341379L;

	public ApiVersionParseException(Token token, String message) {
		super(createMessage(token, message));
	}
	
	public ApiVersionParseException(Token token, String message, Throwable cause) {
		super(createMessage(token, message), cause);
	}
	
	private static String createMessage(Token token, String message) {
		return "At " + token.getLine() + ":" + token.getCharPositionInLine() + ": " + message;
	}

}
