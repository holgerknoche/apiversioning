package apiversioning.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for storing the API key (and thus the identity) of the annotated element. 
 */
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface APIKey {
	
	/**
	 * The value of this API key.
	 * @return see above
	 */
	public long value();

}
