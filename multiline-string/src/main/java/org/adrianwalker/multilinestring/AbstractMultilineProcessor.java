package org.adrianwalker.multilinestring;

import org.adrianwalker.multilinestring.support.StringProcessorHolder;
import org.adrianwalker.multilinestring.support.StringProcessorStrategy;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public abstract class AbstractMultilineProcessor extends AbstractProcessor {

  private final StringProcessorHolder holder;

  public AbstractMultilineProcessor() {
    this.holder = new StringProcessorHolder();
  }

  @Override
  public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
    if (annotations.isEmpty()) {
      return true;
    }

    Collection<? extends TypeElement> collection = Collections.unmodifiableCollection(annotations);
    for (TypeElement typeElement : collection) {
      Set<? extends Element> fields =
        roundEnv.getElementsAnnotatedWith(typeElement);
      StringProcessorStrategy strategy =
        this.holder.getStrategy(typeElement.getQualifiedName().toString());
      processMultiline(fields, strategy);
    }

    return true;
  }

  abstract void processMultiline(final Set<? extends Element> fields, final StringProcessorStrategy strategy);
}
