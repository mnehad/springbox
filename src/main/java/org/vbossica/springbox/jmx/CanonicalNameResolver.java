/*
 * Copyright 2012 the original author or authors.
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
package org.vbossica.springbox.jmx;

/**
 * Implementation of this interface are responsible for defining the final JMX object name of a Spring bean.
 *
 * @author vladimir
 */
public interface CanonicalNameResolver {

  /**
   * Returns the final JMX bean name, based on the original {@code canonicalName} determined by Spring, or {@code null}
   * if this resolver cannot figure out the final name.
   *
   * @param canonicalName
   *     the object name determined by the Spring {@linkplain org.springframework.jmx.export.naming
   *     .MetadataNamingStrategy} class
   *
   * @return the new name, or {@code null} if none could be resolved
   */
  String resolve( String canonicalName );

}
