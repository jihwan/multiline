package org.adrianwalker.multilinestring;

import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import org.adrianwalker.multilinestring.support.StringProcessorStrategy;
import org.eclipse.jdt.internal.compiler.apt.model.VariableElementImpl;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.StringLiteral;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;

public final class EcjMultilineProcessor extends AbstractMultilineProcessor {

	private Elements elementUtils;
	
	@Override
	public void init(final ProcessingEnvironment procEnv) {
		super.init(procEnv);
		this.elementUtils = procEnv.getElementUtils();
	}
	
	@Override
	void processMultiline(final RoundEnvironment roundEnv, TypeElement annotation, StringProcessorStrategy strategy) {
		Set<? extends Element> fields = roundEnv.getElementsAnnotatedWith(annotation);

		for (Element field : fields) {
			String docComment = elementUtils.getDocComment(field);
		
			if (null != docComment) {
				VariableElementImpl fieldElem = (VariableElementImpl) field;
				FieldBinding biding = (FieldBinding) fieldElem._binding;
				FieldDeclaration decl = biding.sourceField();
				StringLiteral string = new StringLiteral(
						strategy.processString(docComment, fieldElem).toCharArray(),
						decl.sourceStart,
						decl.sourceEnd, 
						decl.sourceStart);
				decl.initialization = string;
			}
		}
	}
}
