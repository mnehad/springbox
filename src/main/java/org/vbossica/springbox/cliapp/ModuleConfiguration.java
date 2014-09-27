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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to let a class be automatically discovered by the {@link ModuleLauncher} application.
 *
 * @author vladimir
 */
@Target( { ElementType.TYPE } )
@Retention( value = RetentionPolicy.RUNTIME )
public @interface ModuleConfiguration {

  /**
   * Name of the module; will be used by the CLI application to invoke the underlying code.
   * @return name of the module
   */
  String name();

  /**
   * Description of the module; will be printed on the console when registered modules are listed.
   * @return description of the module
   */
  String description();

}
