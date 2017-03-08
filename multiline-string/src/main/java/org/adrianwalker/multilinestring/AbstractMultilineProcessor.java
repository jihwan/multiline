package org.adrianwalker.multilinestring;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import org.adrianwalker.multilinestring.support.StringProcessorHolder;
import org.adrianwalker.multilinestring.support.StringProcessorStrategy;

@SupportedAnnotationTypes({"org.adrianwalker.multilinestring.Multiline", "org.adrianwalker.multilinestring.SqlMultiline"})
public abstract class AbstractMultilineProcessor extends AbstractProcessor {
	
	private final StringProcessorHolder holder;
	
	public AbstractMultilineProcessor() {
		this.holder = new StringProcessorHolder();
	}
	
	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latest();
	}

	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		if (annotations.isEmpty()) {
			return true;
		}
		
		Collection<? extends TypeElement> collection = Collections.unmodifiableCollection(annotations);
		for (TypeElement typeElement : collection) {
			StringProcessorStrategy strategy = 
					this.holder.getStrategy(typeElement.getQualifiedName().toString());
			processMultiline(roundEnv, typeElement, strategy);
		}
		
		return true;
	}
	
	abstract void processMultiline(final RoundEnvironment roundEnv, TypeElement annotation, StringProcessorStrategy strategy);
}
