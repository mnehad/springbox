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
import org.apache.commons.cli.Options;

/**
 * Runtime definition of a module.
 *
 * @author vladimir
 */
public interface Module {

  /**
   * Returns the list of {@link Options} supported by the module or {@code null} if not applicable.
   * @return options supported by the module
   */
  Options getOptions();

  /**
   * Processes the module with the given {@link CommandLine}.
   * @param cmd Apache command-line object
   * @throws Exception when anything went wrong
   */
  void process( final CommandLine cmd ) throws Exception;

}
