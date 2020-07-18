package apiversioning.parser;

import static apiversioning.parser.ParseUtil.asText;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.Token;

import apiversioning.model.Namespace;
import apiversioning.model.Structure;
import apiversioning.model.Type;
import apiversioning.parser.ApiVersionParser.NamespaceDeclarationContext;
import apiversioning.parser.ApiVersionParser.StructureDeclarationContext;

public class StructureScanner extends ApiVersionBaseVisitor<Void> {

	private Set<String> knownNamespaceNames = new HashSet<>();
	
	private Map<Token, Namespace> tokenToNamespaceMap = new HashMap<>();
	
	private Set<String> knownTypeNames = new HashSet<>();
	
	private Map<Token, Type> tokenToTypeMap = new HashMap<>();
	
	private Map<String, Type> fqNameToTypeMap = new HashMap<>();	
	
	private Namespace currentNamespace;	
	
	@Override
	public Void visitNamespaceDeclaration(NamespaceDeclarationContext ctx) {		
		var namespaceName = asText(ctx.name);		
		var namespace = new Namespace(namespaceName);
		
		this.registerNamespace(ctx.refToken, namespace);
		this.currentNamespace = namespace;
		
		return super.visitNamespaceDeclaration(ctx);
	}
	
	private void registerNamespace(Token refToken, Namespace namespace) {
		String namespaceName = namespace.getName();
		
		if (this.knownNamespaceNames.contains(namespaceName)) {
			throw new ApiVersionParseException(refToken, "Duplicate namespace '" + namespaceName + "'.");
		}
		
		this.knownNamespaceNames.add(namespaceName);		
		this.tokenToNamespaceMap.put(refToken, namespace);
	}
	
	@Override
	public Void visitStructureDeclaration(StructureDeclarationContext ctx) {
		String structureName = asText(ctx.name);
		boolean fixed = (ctx.fixed != null);
		
		Structure structure = new Structure(this.currentNamespace, structureName, fixed);				
		
		this.registerType(ctx.refToken, structure);
		
		return super.visitStructureDeclaration(ctx);
	}
	
	private void registerType(Token refToken, Type type) {
		String fullyQualifiedTypeName = type.getFullyQualifiedName();
		
		if (this.knownTypeNames.contains(fullyQualifiedTypeName)) {
			throw new ApiVersionParseException(refToken, "Duplicate type '" + fullyQualifiedTypeName + "'.");
		}
		
		this.knownTypeNames.add(fullyQualifiedTypeName);
		this.tokenToTypeMap.put(refToken, type);
		this.fqNameToTypeMap.put(fullyQualifiedTypeName, type);
	}
	
	public StructuralElementLookup getLookup() {
		return new StructuralElementLookup(this.tokenToNamespaceMap, this.tokenToTypeMap, this.fqNameToTypeMap);
	}
		
}
