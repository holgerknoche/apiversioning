package apiversioning.routing;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import apiversioning.annotations.APIKey;

public abstract class ApiKeyRouter implements InvocationHandler {

	protected final Object delegate;

	public ApiKeyRouter(Object delegate) {
		this.delegate = delegate;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
		var desiredApiKey = this.getApiKeyOf(method);

		if (desiredApiKey == null) {
			// No API key on the invoked method, so we try to dispatch it as-is
			return method.invoke(this.delegate, arguments);
		}

		return this.invokeApiMethod(desiredApiKey, arguments);		
	}

	private Long getApiKeyOf(Method method) {
		var keyAnnotation = method.getAnnotation(APIKey.class);

		if (keyAnnotation == null) {
			return null;
		}

		return keyAnnotation.value();
	}

	protected Method findDelegateMethodForKey(Long apiKey) {
		var delegateType = this.delegate.getClass();

		for (var intf : delegateType.getInterfaces()) {
			for (var method : intf.getMethods()) {
				var keyAnnotation = method.getAnnotation(APIKey.class);

				if (keyAnnotation == null) {
					continue;
				}

				var actualKey = keyAnnotation.value();
				if (apiKey.equals(actualKey)) {
					return method;
				}
			}
		}

		throw new IllegalStateException("No method for API key " + apiKey + " on type " + delegateType + ".");
	}
	
	protected abstract Object invokeApiMethod(Long apiKey, Object[] arguments) throws Throwable;

}
