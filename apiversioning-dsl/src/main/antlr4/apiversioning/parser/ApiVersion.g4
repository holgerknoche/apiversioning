grammar ApiVersion;

apiVersionSpecification:
		from=fromStatement?
		namespaces+=namespaceDeclaration*		
		EOF
	;

fromStatement:
		'from' previousVersion=STRING_LITERAL
	;
	
namespaceDeclaration:
		refToken='namespace' name=qualifiedName '{'
			elements+=namespaceElementDeclaration*			
		'}'
	;

namespaceElementDeclaration:
		structureDeclaration
	;

structureDeclaration:
		fixed='fixed'? ordered='ordered'? refToken='structure' name=identifier ('replaces' replacedName=identifier)? '{'
			fields+=fieldDeclaration*
		'}'
	;
	
fieldDeclaration:
		name=identifier ':' (baseType=baseTypeSpec | typeName=qualifiedName) ('replaces' replacedName=identifier)?
	;

baseTypeSpec:
		primitiveType |
		dynamicString |
		fixedString |
		fixedNumeric
	;

primitiveType:
		type=('int32' | 'int64' | 'float32' | 'float64')
	;
	
dynamicString:
		'string'
	;
	
fixedString:
		fixed='fixed'? 'string' '(' length=INT_LITERAL ')'
	;
	
fixedNumeric:
		fixed='fixed'? 'numeric' '(' intPlaces=INT_LITERAL (',' fractionalPlaces=INT_LITERAL)? ')'
	;
	
enumDeclaration:
		refToken='enum' name=identifier ('replaces' replacedName=identifier)? '{'
			members+=enumMemberDeclaration*
		'}'
	;
	
enumMemberDeclaration:
		name=identifier ('replaces' replacedName=identifier)?
	;
	
qualifiedName:
		parts+=identifier ('.' parts+=identifier)*
	;
	
identifier:
		ID | LITERAL_ID
	;
	
// --- Lexer Rules

INT_LITERAL:
		[0-9]+
	;

STRING_LITERAL:
		'"'~["]*'"'
	;

LITERAL_ID:
		'\''~[']*'\''
	;
	
ID:
		[A-Za-z][A-Za-z0-9_]*	
	;
	
WS:
		[ \r\t\n]+ -> skip
	;