package apiversioning.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import apiversioning.model.ApiVersion;

public class TestApiVersionResolver implements ApiVersionResolver {

	private final Map<String, ApiVersion> versions;
	
	public static TestApiVersionResolverBuilder builder() {
		return new TestApiVersionResolverBuilder();
	}
	
	private TestApiVersionResolver(Map<String, ApiVersion> versions) {
		this.versions = versions;
	}
	
	@Override
	public Optional<ApiVersion> resolveVersion(String versionIdentifier) {
		return Optional.ofNullable(this.versions.get(versionIdentifier));
	}
	
	public static class TestApiVersionResolverBuilder {
	
		private Map<String, ApiVersion> registeredVersions = new HashMap<>();
				
		public TestApiVersionResolverBuilder addVersion(String id, ApiVersion version) {
			this.registeredVersions.put(id, version);
			
			return this;
		}
		
		public TestApiVersionResolver build() {
			return new TestApiVersionResolver(this.registeredVersions);
		}
		
	}	

}
