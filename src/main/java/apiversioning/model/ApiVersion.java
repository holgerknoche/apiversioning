package apiversioning.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ApiVersion implements ApiVersionElement {
	
	private final Optional<ApiVersion> predecessor;
	
	private final List<Namespace> namespaces;
	
	private final Map<String, Namespace> namespaceLookup;
	
	public ApiVersion(Optional<ApiVersion> predecessor, List<Namespace> namespaces) {
		this.predecessor = predecessor;
		this.namespaces = namespaces;
		
		this.namespaceLookup = namespaces.stream().collect(Collectors.toMap(ns -> ns.getName(), Function.identity()));
	}
	
	public Optional<ApiVersion> getPredecessor() {
		return this.predecessor;
	}

	public List<Namespace> getNamespaces() {
		return this.namespaces;
	}
	
	public Optional<Namespace> findNamespace(String name) {
		return Optional.ofNullable(this.namespaceLookup.get(name));
	}
		
}
