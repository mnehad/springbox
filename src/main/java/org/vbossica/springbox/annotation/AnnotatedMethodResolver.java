/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vbossica.springbox.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * Support class for resolving all method annotated with a give {@code Annotation}. The code was inspired/borrowed from
 * {@code org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping}. A typical case is when several
 * methods in a bean are annotated with the same annotation.<p>
 *
 * <b>Usage</b><p>
 *
 * The following example shows how to auto discover {@code @Component} classes that have methods annotated with
 * {@code MyAnnotation}:
 *
 * <pre>
 * &#64;Target( { METHOD } )
 * &#64;Retention( RUNTIME )
 * &#64;Documented
 * public @interface MyAnnotation {
 *   // ...
 * }
 *
 * public class MyAnnotationProcessor extends ApplicationObjectSupport {
 *
 *   protected void initApplicationContext() throws BeansException {
 *     super.initApplicationContext();
 *
 *     AnnotatedMethodResolver resolver = new AnnotatedMethodResolver&lt;MyAnnotation&gt;(MyAnnotation.class) {
 *
 *       protected void doWithAnnotatedMethod(String beanName, Object bean, Method annotatedMethod, MyAnnotation annotation ) {
 *         // ...
 *       }
 *     };
 *
 *     for (Map.Entry&lt;String, Object&gt; comp : getApplicationContext().getBeansWithAnnotation(Component.class).entrySet()) {
 *       resolver.traverse(comp.getKey(), comp.getValue());
 *     }
 *   }
 * }
 * </pre>
 *
 * @author vladimir
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class AnnotatedMethodResolver<A extends Annotation> {

	private final static Log logger = LogFactory.getLog( AnnotatedMethodResolver.class );

	private Class<A> annotationClass = null;

	/**
	 * Creates a new instance with the class of the annotation to search for.
	 *
	 * @param annotationClass the annotation's class definition
	 */
	public AnnotatedMethodResolver( Class<A> annotationClass ) {
		this.annotationClass = annotationClass;
	}

	/**
	 * Traverses the {@code bean} in search for annotated methods. Calls the
	 * {@link #doWithAnnotatedMethod(String, Object, Method, Annotation)} for every method found.
	 *
	 * @param beanName the name of the Spring bean
	 * @param bean the Spring bean itself
	 * @see #doWithAnnotatedMethod(String, Object, java.lang.reflect.Method, java.lang.annotation.Annotation)
	 */
	public void traverse( final String beanName, final Object bean ) {
		logger.info( "traversing bean " + beanName );

		Class<?> handlerType = bean.getClass();

		Class<?>[] handlerTypes =
				Proxy.isProxyClass( handlerType ) ?
						handlerType.getInterfaces() :
						new Class<?>[] { handlerType };
		for ( final Class<?> currentHandlerType : handlerTypes ) {
			ReflectionUtils.doWithMethods( currentHandlerType,
					new ReflectionUtils.MethodCallback() {
						public void doWith( Method method ) {
							Method specificMethod = ClassUtils.getMostSpecificMethod( method,
									currentHandlerType );
							A annotation = AnnotationUtils.findAnnotation( method,
									annotationClass );
							if ( annotation != null ) {
								doWithAnnotatedMethod( beanName,
										bean,
										specificMethod,
										annotation );
							}
						}
					},
					ReflectionUtils.NON_BRIDGED_METHODS );
		}
	}

	/**
	 * Called whenever a bean has been found.
	 *
	 * @param beanName the name of the Spring bean
	 * @param bean the Spring bean itself
	 * @param annotatedMethod the annotated method
	 * @param annotation the method's annotation
	 */
	protected abstract void doWithAnnotatedMethod( String beanName, Object bean, Method annotatedMethod, A annotation );

}