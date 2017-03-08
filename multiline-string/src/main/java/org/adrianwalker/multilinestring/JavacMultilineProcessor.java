package org.adrianwalker.multilinestring;

import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.adrianwalker.multilinestring.support.StringProcessorStrategy;

import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;

public final class JavacMultilineProcessor extends AbstractMultilineProcessor {

	private JavacElements elementUtils;
	private TreeMaker maker;
	
	@Override
	public void init(final ProcessingEnvironment procEnv) {
		super.init(procEnv);
		JavacProcessingEnvironment javacProcessingEnv = (JavacProcessingEnvironment) procEnv;
		this.elementUtils = javacProcessingEnv.getElementUtils();
		this.maker = TreeMaker.instance(javacProcessingEnv.getContext());
	}
	
	@Override
	void processMultiline(final RoundEnvironment roundEnv, TypeElement annotation, StringProcessorStrategy strategy) {
		Set<? extends Element> fields = roundEnv.getElementsAnnotatedWith(annotation);

		for (Element field : fields) {
			String docComment = elementUtils.getDocComment(field);
		
			if (null != docComment) {
				
				JCVariableDecl fieldNode = (JCVariableDecl) elementUtils.getTree(field);
				fieldNode.init = maker
						.Literal(strategy.toString(docComment, field));
			}
		}
	}
}
