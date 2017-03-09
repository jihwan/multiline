package org.adrianwalker.multilinestring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *   oracle hint support.
 *   [*+ ... *] => /{@literal *+} ... {@literal *}/
 * </pre>
 *
 * (Ex.)
 * <pre><code>
 *   SELECT [*+ YOUR_ORACLE_HINT_HERE *]
 *   	...
 *   FROM
 *   	...
 * </code></pre>
 * @author Jihwan Hwang
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface SqlMultiline {
	boolean mergeLines() default false;
}
