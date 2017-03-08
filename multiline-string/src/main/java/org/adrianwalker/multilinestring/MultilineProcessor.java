package org.adrianwalker.multilinestring;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * 
 * @author Adrian Walker
 * @author Sanghyuk Jung
 * @author Dave Smith
 */
@SupportedAnnotationTypes({"org.adrianwalker.multilinestring.Multiline", "org.adrianwalker.multilinestring.SqlMultiline"})
public final class MultilineProcessor extends AbstractProcessor {
  private Processor delegator = null;
  
  @Override
  public void init(final ProcessingEnvironment procEnv) {
	  super.init(procEnv);
	  String envClassName = procEnv.getClass().getName();
	  if (envClassName.contains("com.sun.tools")) {
		  delegator = new JavacMultilineProcessor();
	  } else {
//		  delegator = new EcjMultilineProcessor();
			try {
				ClassLoader parentClassLoader = MultilineProcessor.class.getClassLoader();
				URL[] urLs = ((URLClassLoader) parentClassLoader).getURLs();
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				delegator = (Processor) new ProxyClassLoader(urLs, classLoader)
						.loadClass("org.adrianwalker.multilinestring.EcjMultilineProcessor").newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
	  }
	  delegator.init(procEnv);
  }

  @Override
  public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
	  if (delegator == null ) {
		  return true;
	  }
	  return delegator.process(annotations, roundEnv);
  }
  
  @Override
  public SourceVersion getSupportedSourceVersion() {
	  return SourceVersion.latest();
  }
  
  /**
   * @author Dave Smith
   */
  class ProxyClassLoader extends URLClassLoader {
		
		private final ClassLoader contextLoader;
		
		ProxyClassLoader(URL[] urls, ClassLoader contextLoader) {	
			super(urls, contextLoader);
			this.contextLoader = contextLoader;
		}	
		
		/**
		 * <p>
		 * 	getClassLoadingLock method since jdk 1.7
		 * </p>
		 * 
		 */
		@Override
		public synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
//			synchronized (super.getClassLoadingLock(name)) {
				Class<?> c;
				if (!name.startsWith("org.adrianwalker.multilinestring")) {
					c = contextLoader.loadClass(name);
				} else {
					c = findLoadedClass(name);
					if (c == null) {
						try {
							c = findClass(name);
						} catch (ClassNotFoundException ex) {
							return super.loadClass(name, resolve);
						}
					}
				}
				if (resolve) {
					resolveClass(c);
				}
				return c;
//			}
		}
	}
}
