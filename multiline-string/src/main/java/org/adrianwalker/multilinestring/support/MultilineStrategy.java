package org.adrianwalker.multilinestring.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.lang.model.element.Element;

import org.adrianwalker.multilinestring.Multiline;

/**
 * @author Jihwan Hwang
 */
public class MultilineStrategy implements StringProcessorStrategy {
	@Override
	public String toString(String value, Element fieldElem) {
		
		Multiline annotation = Multiline.class.cast(fieldElem.getAnnotation(Multiline.class));

		if (!annotation.mergeLines() && !annotation.trimWhiteSpace()) {
			return value;
		}

		try {
			BufferedReader reader = new BufferedReader(new StringReader(value));
			StringBuilder buf = new StringBuilder();
			String line = reader.readLine();
			while (line != null) {
				if (annotation.trimWhiteSpace()) {
					line = line.trim();
				}
				if (annotation.mergeLines() && buf.length() > 0) {
					buf.append(annotation.mergeChar());
				}
				buf.append(line);
				line = reader.readLine();
			}
			return buf.toString();
		} catch (IOException ex) {
			// should never happen. Just return the value
			return value;
		}
	}
}
