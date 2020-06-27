package apiversioning.parser;

import java.util.Optional;

import apiversioning.model.ApiVersion;

public class IdleApiVersionResolver implements ApiVersionResolver {

	@Override
	public Optional<ApiVersion> resolveVersion(String versionIdentifier) {
		throw new UnsupportedOperationException("A specific resolver needs to be specified for predecessor models.");
	}

}
