package apiversioning.model;

import java.util.List;

public class ApiVersion implements ApiVersionElement {
	
	private final ApiVersion predecessor;
	
	private final List<Namespace> namespaces;
	
	public ApiVersion(ApiVersion predecessor, List<Namespace> namespaces) {
		this.predecessor = predecessor;
		this.namespaces = namespaces;
	}
	
	public ApiVersion getPredecessor() {
		return this.predecessor;
	}

	public List<Namespace> getNamespaces() {
		return this.namespaces;
	}
	
}
