package apiversioning.typing;

import java.lang.reflect.Proxy;
import java.util.function.Function;

import apiversioning.routing.ApiKeyRouter;
import apiversioning.routing.ReflectionBasedApiKeyRouter;

public class Cast {

	private static final Function<Object, ApiKeyRouter> ROUTER_PROVIDER = ReflectionBasedApiKeyRouter::new;
	
	@SuppressWarnings("unchecked")
	public static <T> T castTo(Class<T> targetType, Object object) {
		Class<?>[] interfaces = new Class<?>[] {targetType};
		return (T) Proxy.newProxyInstance(targetType.getClassLoader(), interfaces, ROUTER_PROVIDER.apply(object));
	}

}
