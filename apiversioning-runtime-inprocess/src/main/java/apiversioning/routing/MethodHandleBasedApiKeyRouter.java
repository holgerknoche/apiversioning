package apiversioning.routing;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MethodHandleBasedApiKeyRouter extends ApiKeyRouter {

	private static final ConcurrentMap<Long, MethodHandle> HANDLE_CACHE = new ConcurrentHashMap<>();
	
	public MethodHandleBasedApiKeyRouter(Object delegate) {
		super(delegate);
	}

	@Override
	protected Object invokeApiMethod(Long apiKey, Object[] arguments) throws Throwable {
		var delegateHandle = HANDLE_CACHE.computeIfAbsent(apiKey, this::findHandleForKey);
		return delegateHandle.bindTo(this.delegate).invokeWithArguments(arguments);
	}
	
	private MethodHandle findHandleForKey(Long apiKey) {
		var delegateMethod = this.findDelegateMethodForKey(apiKey);
		
		try {
			return MethodHandles.lookup().unreflect(delegateMethod);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
