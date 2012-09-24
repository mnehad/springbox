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

import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.support.PropertiesLoaderSupport;

/**
 * Specific {@link CanonicalNameResolver} that loads the mappings from a property file. The configuration is similar to
 * the one of the {@link PropertiesLoaderSupport} class, meaning that the {@link PropertiesLoaderSupport#setLocations(org.springframework.core.io.Resource[])}
 * should be used to specify the files containing the mappings.<p>
 *
 * <b>Usage:</b><p>
 *
 * It is important to note that the special characters (':' and '=') in the JMX object names must be correctly escaped:
 * <pre>
 *   org.example.services\:name\=ServiceMBean = com.dummy.services\:name\=ExtraMBean
 * </pre>
 *
 * @author vladimir
 */
public class PropertyCanonicalNameResolver extends PropertiesLoaderSupport
    implements CanonicalNameResolver, InitializingBean {

  private final static Log logger = LogFactory.getLog( PropertyCanonicalNameResolver.class );

  private Properties props;

  @Override
  public void afterPropertiesSet() throws Exception {
    props = mergeProperties();
    if ( logger.isInfoEnabled() ) {
      for ( Map.Entry<Object, Object> entry : props.entrySet() ) {
        logger.info( "objectname jmx mapping: " + entry.getKey().toString() + " => " + entry.getValue().toString() );
      }
    }
  }

  @Override
  public String resolve( final String canonicalName ) {
    Object name = props.get( canonicalName );
    return name == null ?
        null :
        name.toString();
  }

}
