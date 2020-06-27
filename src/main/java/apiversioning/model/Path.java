package apiversioning.model;

import java.util.List;
import java.util.Optional;

public class Path {
	
	private final List<String> parts;
	
	private final int maxIndex;
	
	public Path(List<String> parts) {
		this.parts = parts;
		this.maxIndex = (parts.size() - 1);
	}
	
	public Optional<Namespace> resolveNamespace(ApiVersion version) {
		return this.resolveNamespace(0, version);
	}
	
	protected Optional<Namespace> resolveNamespace(int currentIndex, ApiVersion version) {
		if (!this.hasNext(currentIndex)) {
			return Optional.empty();
		}
				
		var namespaceName = this.parts.get(currentIndex);
		var namespace = version.findNamespace(namespaceName);
		
		if (!namespace.isPresent()) {
			return Optional.empty();
		} else {
			return this.resolveNamespace(++currentIndex, namespace.get());
		}
	}
	
	protected Optional<Namespace> resolveNamespace(int currentIndex, Namespace namespace) {
		if (!this.hasNext(currentIndex)) {
			return Optional.of(namespace);
		}
		
		var subNamespaceName = this.parts.get(currentIndex);
		var subNamespace = namespace.findSubNamespace(subNamespaceName);
		
		if (!subNamespace.isPresent()) {
			return Optional.empty();
		} else {
			return this.resolveNamespace(++currentIndex, subNamespace.get());
		}
	}
	
	protected Optional<Type> resolveType(ApiVersion version) {
		return this.resolveType(0, version);
	}
	
	protected Optional<Type> resolveType(int currentIndex, ApiVersion version) {
		if (!this.hasNext(currentIndex)) {
			return Optional.empty();
		}
				
		// API versions cannot contain types directly
		var namespaceName = this.parts.get(currentIndex);
		var namespace = version.findNamespace(namespaceName);
		
		if (!namespace.isPresent()) {
			return Optional.empty();
		} else {
			return this.resolveType(++currentIndex, namespace.get());
		}
	}
	
	public Optional<Type> resolveType(Namespace namespace) {
		return this.resolveType(0, namespace);
	}
	
	protected Optional<Type> resolveType(int currentIndex, Namespace namespace) {
		if (!this.hasNext(currentIndex)) {
			return Optional.empty();
		}
				
		var elementName = this.parts.get(currentIndex);
		
		if (this.hasAtLeastOneMore(currentIndex)) {
			// If we have at least one more elements in the path, we are looking
			// for a namespace (type names must be last)
			var subNamespace = namespace.findSubNamespace(elementName);
		
			if (!subNamespace.isPresent()) {
				return Optional.empty();
			} else {
				return this.resolveType(++currentIndex, subNamespace.get());
			}
		} else {
			// Otherwise, we are looking for a type
			return namespace.findType(elementName);
		}
	}
	
	public Optional<Field> resolveField(ApiVersion version) {
		return this.resolveField(0, version);
	}
	
	protected Optional<Field> resolveField(int currentIndex, ApiVersion version) {
		if (!this.hasNext(currentIndex)) {
			return Optional.empty();
		}
				
		// API versions cannot contain fields directly
		var namespaceName = this.parts.get(currentIndex);
		var namespace = version.findNamespace(namespaceName);
		
		if (!namespace.isPresent()) {
			return Optional.empty();
		} else {
			return this.resolveField(++currentIndex, namespace.get());
		}
	}
	
	protected Optional<Field> resolveField(int currentIndex, Namespace namespace) {
		if (!this.hasNext(currentIndex)) {
			return Optional.empty();
		}
				
		var elementName = this.parts.get(currentIndex);
		
		if (this.hasAtLeastTwoMore(currentIndex)) {
			// If we have at least two more elements in the path, we are looking
			// for a namespace (type and field names must be last)
			var subNamespace = namespace.findSubNamespace(elementName);
		
			if (!subNamespace.isPresent()) {
				return Optional.empty();
			} else {
				return this.resolveField(++currentIndex, subNamespace.get());
			}
		} else if (this.hasAtLeastOneMore(currentIndex)) {
			// Otherwise, we are looking for a type
			var type = namespace.findType(elementName);
			
			if (!type.isPresent()) {
				return Optional.empty();
			} else {
				return this.resolveField(++currentIndex, type.get());
			}
		} else {
			// Namespaces cannot contain fields directly
			return Optional.empty();
		}
	}
	
	public Optional<Field> resolveField(Type type) {
		return this.resolveField(0, type);
	}
	
	protected Optional<Field> resolveField(int currentIndex, Type type) {
		if (!this.isAtLastElement(currentIndex)) {
			return Optional.empty();
		}
		
		var fieldName = this.parts.get(currentIndex);
		
		return type.findField(fieldName);
	}
	
	private boolean hasNext(int currentIndex) {
		return (currentIndex <= this.maxIndex);
	}
	
	private boolean hasAtLeastOneMore(int currentIndex) {
		return (currentIndex < this.maxIndex);
	}
	
	private boolean hasAtLeastTwoMore(int currentIndex) {
		return ((currentIndex + 1) < this.maxIndex);
	}		
	
	private boolean isAtLastElement(int currentIndex) {
		return (currentIndex == this.maxIndex);
	}
	
}
