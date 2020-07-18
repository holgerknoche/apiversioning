package apiversioning.parser;

import java.util.Optional;

import apiversioning.model.ApiVersion;

public interface ApiVersionResolver {
	
	public Optional<ApiVersion> resolveVersion(String versionIdentifier);

}
