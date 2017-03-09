package org.adrianwalker.multilinestring.support;

import java.util.HashMap;
import java.util.Map;

import org.adrianwalker.multilinestring.Multiline;
import org.adrianwalker.multilinestring.SqlMultiline;

/**
 * @author Jihwan Hwang
 */
public class StringProcessorHolder {
	
	Map<String, StringProcessorStrategy> map = new HashMap<String, StringProcessorStrategy>(2);

	public StringProcessorHolder() {
		map.put(Multiline.class.getName(), new MultilineStrategy());
		map.put(SqlMultiline.class.getName(), new SqlMultilineStrategy());
	}
	
	public StringProcessorStrategy getStrategy(String qualifiedClassName) {
		if (map.containsKey(qualifiedClassName) == false) {
			throw new IllegalArgumentException(qualifiedClassName + " is not support");
		}
		
		return map.get(qualifiedClassName);
	}
}
