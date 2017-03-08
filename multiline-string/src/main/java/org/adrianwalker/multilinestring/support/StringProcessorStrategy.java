package org.adrianwalker.multilinestring.support;

import javax.lang.model.element.Element;

/**
 * @author Jihwan Hwang
 */
public interface StringProcessorStrategy {
	
	String CRNL = System.getProperty("line.separator");
	
	String toString(String value, Element fieldElem);
}