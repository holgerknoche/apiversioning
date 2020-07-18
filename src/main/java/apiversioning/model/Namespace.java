package apiversioning.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Namespace {
	
	private final String name;
		
	private final List<Namespace> subNamespaces = new ArrayList<>();
	
	private final Map<String, Namespace> subNamespaceLookup = new HashMap<>();
	
	private final List<Type> types = new ArrayList<>();
	
	private final Map<String, Type> typeLookup = new HashMap<>();
	
	private Optional<Namespace> predecessor = Optional.empty();
	
	public Namespace(String name) {
		this.name = name;
	}
	
	public Optional<Namespace> getPredecessor() {
		return this.predecessor;
	}
	
	public void setPredecessor(Optional<Namespace> predecessor) {
		this.predecessor = predecessor;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void addType(Type type) {
		this.types.add(type);
		
		var previousType = this.typeLookup.put(type.getName(), type);
		if (previousType != null) {
			throw new IllegalArgumentException("Duplicate type '" + type.getName() + "' in namespace " + this.getName() + ".");
		}
	}
	
	public Optional<Type> findType(String name) {
		return Optional.ofNullable(this.typeLookup.get(name));
	}
	
	public void addSubNamespace(Namespace namespace) {
		if (namespace.containsNamespace(this)) {
			// Cycle detected
			throw new IllegalArgumentException("Cyclic dependency between namespaces " + this.getName() + " and " + namespace.getName() + ".");
		}
		
		this.subNamespaces.add(namespace);
		this.subNamespaceLookup.put(namespace.getName(), namespace);
	}
	
	private boolean containsNamespace(Namespace namespace) {
		if (this.equals(namespace)) {
			return true;
		}
		
		for (var subNamespace : this.subNamespaces) {
			if (subNamespace.containsNamespace(namespace)) {
				return true;
			}
		}
		
		return false;
	}

	public Optional<Namespace> findSubNamespace(String namespaceName) {
		return Optional.ofNullable(this.subNamespaceLookup.get(namespaceName));
	}
	
	public Path getPath() {
		return new Path(Arrays.asList(this.getName()));
	}
	
	@Override
	public String toString() {
		return "Namespace " + this.getName();
	}

}
