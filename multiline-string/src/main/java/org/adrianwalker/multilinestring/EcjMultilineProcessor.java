package org.adrianwalker.multilinestring;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import org.eclipse.jdt.internal.compiler.apt.model.VariableElementImpl;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.StringLiteral;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;

@SupportedAnnotationTypes({"org.adrianwalker.multilinestring.Multiline"})
public final class EcjMultilineProcessor extends AbstractProcessor {

	private Elements elementUtils;
  
	@Override
	public void init(final ProcessingEnvironment procEnv) {
		super.init(procEnv);
		this.elementUtils = procEnv.getElementUtils();
	}

	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		Set<? extends Element> fields = roundEnv.getElementsAnnotatedWith(Multiline.class);

		for (Element field : fields) {
			String docComment = elementUtils.getDocComment(field);
		
			if (null != docComment) {
				VariableElementImpl fieldElem = (VariableElementImpl) field;
				FieldBinding biding = (FieldBinding) fieldElem._binding;
				FieldDeclaration decl = biding.sourceField();
				StringLiteral string = new StringLiteral(StringProcessor.toString(docComment,fieldElem.getAnnotation(Multiline.class)).toCharArray(), decl.sourceStart ,decl.sourceEnd, decl.sourceStart);
				decl.initialization = string;
			}
		}
		return true;
	}
}
