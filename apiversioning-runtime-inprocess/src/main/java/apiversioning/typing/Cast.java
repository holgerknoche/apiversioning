package apiversioning.typing;

import java.lang.reflect.Proxy;

import apiversioning.routing.ApiKeyRouter;

public class Cast {

	@SuppressWarnings("unchecked")
	public static <T> T castTo(Class<T> targetType, Object object) {
		Class<?>[] interfaces = new Class<?>[] {targetType};
		return (T) Proxy.newProxyInstance(targetType.getClassLoader(), interfaces, new ApiKeyRouter(object));
	}

}
