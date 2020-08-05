package apiversioning.routing;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ReflectionBasedApiKeyRouter extends ApiKeyRouter {

	private static final ConcurrentMap<Long, Method> METHOD_CACHE = new ConcurrentHashMap<>();
	
	public ReflectionBasedApiKeyRouter(Object delegate) {
		super(delegate);
	}

	@Override
	protected Object invokeApiMethod(Long apiKey, Object[] arguments) throws Throwable {
		var delegateMethod = METHOD_CACHE.computeIfAbsent(apiKey, this::findDelegateMethodForKey);
		return delegateMethod.invoke(this.delegate, arguments);
	}	
	
}
