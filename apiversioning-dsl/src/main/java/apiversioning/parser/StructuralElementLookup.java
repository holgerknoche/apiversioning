package apiversioning.parser;

import java.util.Map;

import org.antlr.v4.runtime.Token;

import apiversioning.model.Namespace;
import apiversioning.model.Type;

class StructuralElementLookup {
	
	private final Map<Token, Namespace> tokenToNamespaceLookup;
	
	private final Map<Token, Type> tokenToTypeLookup;
	
	private final Map<String, Type> fqNameToTypeLookup;
	
	public StructuralElementLookup(Map<Token, Namespace> tokenToNamespaceLookup, Map<Token, Type> tokenToTypeLookup, Map<String, Type> fqNameToTypeLookup) {
		this.tokenToNamespaceLookup = tokenToNamespaceLookup;
		this.tokenToTypeLookup = tokenToTypeLookup;
		this.fqNameToTypeLookup = fqNameToTypeLookup;
	}
	
	public Namespace lookupNamespaceByToken(Token token) {
		return this.tokenToNamespaceLookup.get(token);
	}
	
	public Type lookupTypeByToken(Token token) {
		return this.tokenToTypeLookup.get(token);
	}
	
	public Type lookupTypeByFQName(String fqName) {
		return this.fqNameToTypeLookup.get(fqName);
	}

}
