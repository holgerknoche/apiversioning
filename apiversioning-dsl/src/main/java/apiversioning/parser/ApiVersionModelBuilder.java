package apiversioning.parser;

import static apiversioning.parser.ParseUtil.asText;
import static apiversioning.parser.ParseUtil.unquote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import apiversioning.model.ApiVersion;
import apiversioning.model.Field;
import apiversioning.model.Namespace;
import apiversioning.model.NumericType;
import apiversioning.model.Path;
import apiversioning.model.PrimitiveType;
import apiversioning.model.StringType;
import apiversioning.model.Structure;
import apiversioning.model.Type;
import apiversioning.parser.ApiVersionParser.ApiVersionSpecificationContext;
import apiversioning.parser.ApiVersionParser.BaseTypeSpecContext;
import apiversioning.parser.ApiVersionParser.DynamicStringContext;
import apiversioning.parser.ApiVersionParser.FieldDeclarationContext;
import apiversioning.parser.ApiVersionParser.FixedNumericContext;
import apiversioning.parser.ApiVersionParser.FixedStringContext;
import apiversioning.parser.ApiVersionParser.FromStatementContext;
import apiversioning.parser.ApiVersionParser.IdentifierContext;
import apiversioning.parser.ApiVersionParser.NamespaceDeclarationContext;
import apiversioning.parser.ApiVersionParser.PrimitiveTypeContext;
import apiversioning.parser.ApiVersionParser.QualifiedNameContext;
import apiversioning.parser.ApiVersionParser.StructureDeclarationContext;

class ApiVersionModelBuilder extends ApiVersionBaseVisitor<Void> {
		
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiVersionModelBuilder.class);
	
	private final ApiVersionResolver versionResolver;
	
	private final StructuralElementLookup elementLookup;
	
	private Optional<ApiVersion> predecessor;

	private List<Namespace> namespaces;
	
	private Namespace currentNamespace;

	private Structure currentStructure;
	
	public ApiVersionModelBuilder(ApiVersionResolver versionResolver, StructuralElementLookup elementLookup) {
		this.versionResolver = versionResolver;
		this.elementLookup = elementLookup;
	}
	
	public ApiVersion buildApiVersionFrom(ApiVersionSpecificationContext specification) {
		// Clear work fields
		this.predecessor = Optional.empty();
		this.namespaces = new ArrayList<>();
		this.currentNamespace = null;
		this.currentStructure = null;
		
		specification.accept(this);
				
		return new ApiVersion(this.predecessor, this.namespaces);
	}
	
	@Override
	public Void visitFromStatement(FromStatementContext ctx) {
		// Retrieve and resolve the predecessor model ID
		var predecessorId = unquote(ctx.previousVersion.getText());
		var predecessor = this.versionResolver.resolveVersion(predecessorId);
		
		if (!predecessor.isPresent()) {
			throw new ApiVersionParseException(ctx.previousVersion, "Predecessor model '" + predecessorId + "' could not be resolved.");
		}
		
		this.predecessor = predecessor;
		
		return null;
	}	

	@Override
	public Void visitNamespaceDeclaration(NamespaceDeclarationContext ctx) {		
		// Look up the namespace using the reference token
		var namespace = this.elementLookup.lookupNamespaceByToken(ctx.refToken);
		
		this.namespaces.add(namespace);
		this.currentNamespace = namespace;
		
		// Try to find a predecessor for this type if the model has a predecessor
		if (this.predecessor.isPresent()) {
			// TODO Currently, we do not support namespace replacements
			Path path = namespace.getPath();
			var predecessorNamespace = path.resolveNamespace(this.predecessor.get());
			
			namespace.setPredecessor(predecessorNamespace);
			LOGGER.debug("Predecessor of '%s' was resolved to '%s'.", namespace, predecessorNamespace);
		}
		
		return super.visitNamespaceDeclaration(ctx);
	}	
	
	@Override
	public Void visitStructureDeclaration(StructureDeclarationContext ctx) {
		// Look up the structure using the reference token
		var structure = (Structure) this.elementLookup.lookupTypeByToken(ctx.refToken);
		
		this.currentNamespace.addType(structure);
		this.currentStructure = structure;
		
		// Resolve predecessor, if necessary
		if (ctx.replacedName != null) {
			// Explicit replacement
			var optPredecessorNamespace = this.currentNamespace.getPredecessor();
			
			if (!optPredecessorNamespace.isPresent()) {
				throw new IllegalArgumentException("No predecessor for namespace '" + this.currentNamespace.getName() + "', though contained type '" + structure.getName() + "' is an explicit replacement.");
			}
			
			var predecessorNamespace = optPredecessorNamespace.get(); 
			var path = asPath(ctx.replacedName);
			
			var predecessorType = path.resolveType(predecessorNamespace);
			
			if (!predecessorType.isPresent()) {
				throw new IllegalArgumentException("The specified predecessor type '" + path + "' cannot be found.");
			}
			
			this.updatePredecessorForStructure(structure, predecessorType);			
		} else if (this.currentNamespace.getPredecessor().isPresent()) {
			// Implicit replacement (the structure may have existed before)
			var predecessorNamespace = this.currentNamespace.getPredecessor().get();
			var path = asPath(ctx.name);
			
			var predecessorType = path.resolveType(predecessorNamespace);
			this.updatePredecessorForStructure(structure, predecessorType);
		}
		
		return super.visitStructureDeclaration(ctx);
	}
	
	private void updatePredecessorForStructure(Structure structure, Optional<Type> predecessor) {
		if (predecessor.isPresent()) {
			Type predecessorType = predecessor.get();
			
			if (predecessorType instanceof Structure) {
				structure.setPredecessor(Optional.of((Structure) predecessorType));
			} else {
				throw new IllegalArgumentException("Predecessor of " + structure.getFullyQualifiedName() + "' is not a structure.");
			}
		}
	}
	
	@Override
	public Void visitFieldDeclaration(FieldDeclarationContext ctx) {
		var fieldName = asText(ctx.name);
		var fieldType = this.resolveType(ctx.baseType, ctx.typeName);
			
		var field = new Field(this.currentStructure, fieldName, fieldType);
		
		// Resolve predecessor, if appropriate
		if (ctx.replacedName != null) {
			// Explicit replacement
			var optPredecessorStructure = this.currentStructure.getPredecessor();
			
			if (!optPredecessorStructure.isPresent()) {
				throw new IllegalArgumentException("No predecessor for structure '" + this.currentStructure.getName() + "', though contained type '" + field.getName() + "' is an explicit replacement.");
			}
			
			var predecessorStructure = optPredecessorStructure.get();
			var path = asPath(ctx.replacedName);
			
			var predecessor = path.resolveField(predecessorStructure);
			
			if (!predecessor.isPresent()) {
				throw new IllegalArgumentException("The specified predecessor field '" + path + "' cannot be found.");
			}
			
			field.setPredecessor(predecessor);			
		} else if (this.currentStructure.getPredecessor().isPresent()) {
			// Implicit replacement
			var predecessorStructure = this.currentStructure.getPredecessor().get();
			var path = asPath(ctx.name);
			
			var predecessor = path.resolveField(predecessorStructure);
			field.setPredecessor(predecessor);
		}		
		
		// Check for dynamic fields in fixed structures
		if (this.currentStructure.isFixed() && !field.getType().isFixed()) {
			throw new ApiVersionParseException(ctx.name.start, "Dynamic field '" + fieldName + "' in fixed structure '" + this.currentStructure.getFullyQualifiedName() + "'.");
		}
		
		this.currentStructure.addField(field);
		
		return super.visitFieldDeclaration(ctx);
	}
	
	private static Path asPath(QualifiedNameContext qualifiedName) {
		List<String> parts = new ArrayList<>(qualifiedName.parts.size());
		
		for (var part : qualifiedName.parts) {
			parts.add(part.start.getText());
		}
		
		return new Path(parts);
	}
	
	private static Path asPath(IdentifierContext identifier) {
		return asPath(identifier.start.getText());
	}
	
	private static Path asPath(String onlyPart) {
		return new Path(Arrays.asList(onlyPart));
	}
	
	private Type resolveType(BaseTypeSpecContext baseType, QualifiedNameContext qualifiedName) {
		return (baseType != null) ? this.resolveBaseType(baseType) : this.resolveUserDefinedType(qualifiedName);
	}
	
	private Type resolveBaseType(BaseTypeSpecContext baseType) {
		return new BaseTypeResolver().resolveType(baseType);
	}
	
	private Type resolveUserDefinedType(QualifiedNameContext qualifiedName) {
		String typeName = asText(qualifiedName);
		
		// We do not know if the name is actually fully qualified, it could just be
		// a single name. Therefore, we first assume that it is indeed fully qualified,
		// and try to look it up as it is.
		Type type = this.elementLookup.lookupTypeByFQName(typeName);
		if (type != null) {
			return type;
		}
		
		// If the first lookup failed, we assume that the name is not fully qualified 
		// and try to qualify it by prepending the current namespace
		String extendedTypeName = this.currentNamespace.getName() + "." + typeName;
		type = this.elementLookup.lookupTypeByFQName(extendedTypeName);
		if (type != null) {
			return type;
		}
		
		// Throw an exception if neither worked
		throw new ApiVersionParseException(qualifiedName.start, "Unknown type '" + typeName + "'.");
	}
	
	private static class BaseTypeResolver extends ApiVersionBaseVisitor<Type> {
		
		public Type resolveType(BaseTypeSpecContext baseType) {
			return baseType.accept(this);
		}
		
		@Override
		public Type visitPrimitiveType(PrimitiveTypeContext ctx) {
			var refToken = ctx.type;
			var typeName = refToken.getText();
			
			var type = PrimitiveType.forLiteral(typeName);
			if (type == null) {
				throw new ApiVersionParseException(refToken, "Unknown primitive type '" + typeName + "'.");
			}

			return type;
		}
		
		@Override
		public Type visitDynamicString(DynamicStringContext ctx) {
			return StringType.ofDynamicLength();
		}
		
		@Override
		public Type visitFixedString(FixedStringContext ctx) {
			var length = Integer.parseInt(ctx.length.getText());
			
			return StringType.ofFixedLength(length);
		}
		
		@Override
		public Type visitFixedNumeric(FixedNumericContext ctx) {
			var integerPlaces = Integer.parseInt(ctx.intPlaces.getText());
			var fractionalPlaces = (ctx.fractionalPlaces == null) ? 0 : Integer.parseInt(ctx.fractionalPlaces.getText());
			
			return new NumericType(integerPlaces, fractionalPlaces);
		}
		
	}
	
}
