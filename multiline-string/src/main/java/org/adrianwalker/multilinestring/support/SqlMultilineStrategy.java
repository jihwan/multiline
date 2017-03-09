package org.adrianwalker.multilinestring.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.lang.model.element.Element;

import org.adrianwalker.multilinestring.SqlMultiline;

/**
 * @author Jihwan Hwang
 */
public class SqlMultilineStrategy implements StringProcessorStrategy {
	
	final char MERGE_CHAR = ' ';
	
	@Override
	public String processString(String value, Element fieldElem) {

		SqlMultiline annotation = SqlMultiline.class.cast(fieldElem.getAnnotation(SqlMultiline.class));

		if (value.contains("[*") || value.contains("*]")) {
			value = value.replaceAll("\\[\\*", "/*");
			value = value.replaceAll("\\*\\]", "*/");
		}

		if (!annotation.mergeLines()) {
			return value;
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new StringReader(value));
			StringBuilder buf = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				if (annotation.mergeLines() && buf.length() > 0) {
					buf.append(MERGE_CHAR);
				}
				buf.append(line);
			}
			return buf.toString();
		} catch (IOException ex) {
			// should never happen. Just return the value
			return value;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
