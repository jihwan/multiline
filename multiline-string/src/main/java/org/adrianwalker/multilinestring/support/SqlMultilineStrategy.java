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
	@Override
	public String toString(String value, Element fieldElem) {

		SqlMultiline annotation = SqlMultiline.class.cast(fieldElem.getAnnotation(SqlMultiline.class));

		if (value.contains("[*") || value.contains("*]")) {
			value = value.replaceAll("\\[\\*", "/*");
			value = value.replaceAll("\\*\\]", "*/");
		}

		if (!annotation.mergeLines()) {
			return value;
		}

		try {
			BufferedReader reader = new BufferedReader(new StringReader(value));
			StringBuilder buf = new StringBuilder();
			String line = reader.readLine();
			while (line != null) {
				if (annotation.mergeLines() && buf.length() > 0) {
					// buf.append(annotation.mergeChar());
					if (annotation.mergeChar() != '\0') {
						buf.append(annotation.mergeChar());
					}
				}
				buf.append(line);
				if (!annotation.mergeLines()) {
					buf.append(CRNL);
				}

				line = reader.readLine();
			}
			return buf.toString();
		} catch (IOException ex) {
			// should never happen. Just return the value
			return value;
		}
	}
}
