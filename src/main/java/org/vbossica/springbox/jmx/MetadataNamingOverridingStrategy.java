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

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jmx.export.naming.MetadataNamingStrategy;
import org.springframework.jmx.export.naming.ObjectNamingStrategy;
import org.springframework.jmx.support.ObjectNameManager;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * An implementation of the {@link ObjectNamingStrategy} interface that reads the {@code ObjectName} form the
 * source-level metadata but allows it to be overwritten using a provided {@link CanonicalNameResolver}.
 *
 * @author vladimir
 */
public class MetadataNamingOverridingStrategy extends MetadataNamingStrategy {

  private final static Log logger = LogFactory.getLog( MetadataNamingOverridingStrategy.class );

  private CanonicalNameResolver resolver = new CanonicalNameResolver() {

    @Override
    public String resolve( String canonicalName ) {
      return null;
    }
  };

  public void setResolver( final CanonicalNameResolver resolver ) {
    Assert.notNull( resolver, "resolver cannot be null" );
    this.resolver = resolver;
  }

  @Override
  public ObjectName getObjectName( Object managedBean, String beanKey ) throws MalformedObjectNameException {
    ObjectName objectName = super.getObjectName( managedBean, beanKey );
    String finalName = resolver.resolve( objectName.getCanonicalName() );

    if ( finalName != null && StringUtils.hasText( finalName ) ) {
      logger.info( "replacing " + objectName + " by " + finalName );
      return ObjectNameManager.getInstance( finalName );
    }
    return objectName;
  }

}
