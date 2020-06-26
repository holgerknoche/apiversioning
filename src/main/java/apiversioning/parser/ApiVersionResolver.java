package apiversioning.parser;

import apiversioning.model.ApiVersion;

public interface ApiVersionResolver {
	
	public ApiVersion resolveVersion(String versionIdentifier);

}
