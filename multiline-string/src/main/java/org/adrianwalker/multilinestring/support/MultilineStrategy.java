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
	public String processString(String value, Element fieldElem) {
		
		Multiline annotation = Multiline.class.cast(fieldElem.getAnnotation(Multiline.class));

		if (!annotation.mergeLines() && !annotation.trimWhiteSpace()) {
			return value;
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new StringReader(value));
			StringBuilder buf = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				if (annotation.trimWhiteSpace()) {
					line = line.trim();
				}
				if (annotation.mergeLines() && buf.length() > 0) {
					if(annotation.mergeChar() != '\0') {
						buf.append(annotation.mergeChar());
					}
				}
				buf.append(line);
				if(!annotation.mergeLines()) {
					buf.append(CRNL);
				}
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
