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
package org.vbossica.springbox.cliapp;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Base class for Spring specific modules. Loads the {@link ApplicationContext} from the classpath.
 *
 * @author vladimir
 */
public abstract class AbstractSpringModule implements Module {

  @Override
  public final void process( final CommandLine cmd ) throws Exception {
    String contextName = getApplicationContextFilename();
    ApplicationContext context = new ClassPathXmlApplicationContext( contextName );

    doProcess( cmd, context );
  }

  /**
   * Returns the name of the Spring application context to load. By default, the name is the lowercased name of the
   * module class, with the words separated by an underscore.<p>
   *
   * <b>Example</b>
   *
   * <ul>
   *   <li>{@code SampleModule.class} =&gt; {@code META-INF/sample_module.xml}</li>
   * </ul>
   * @return filename of the application context
   */
  protected String getApplicationContextFilename() {
    String filename = StringUtils.join(
            StringUtils.splitByCharacterTypeCamelCase(ClassUtils.getShortClassName( getClass() )), '_')
            .toLowerCase();
    return "META-INF/" + filename + ".xml";
  }

  /**
   * Processes the loaded {@link ApplicationContext} with the given {@link CommandLine} arguments.
   * @param cmd Apache command-line object
   * @param context Spring application context
   */
  protected abstract void doProcess( final CommandLine cmd, ApplicationContext context );

}
