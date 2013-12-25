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
package org.vbossica.springbox.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Utility class to debug from where the heck Spring loads the properties from.
 *
 * @author vladimir
 */
public class PropertyPlaceholderDebuggingConfigurer extends PropertyPlaceholderConfigurer {

  private static Logger log = LoggerFactory.getLogger(PropertyPlaceholderDebuggingConfigurer.class);

  public void setLocations(Resource[] locations) {
    super.setLocations(locations);
    if (log.isDebugEnabled()) {
      log.debug("Setting locations");
      for (Resource location : locations) {
        log.debug("resource: {}", location.getFilename());
      }
    }
  }

  @Override
  protected void loadProperties(Properties props) throws IOException {
    super.loadProperties(props);

    if (log.isDebugEnabled()) {
      log.debug("Loading properties");
      for (Map.Entry<Object, Object> entry : props.entrySet()) {
        log.debug("{}: '{}'", entry.getKey(), entry.getValue());
      }
    }
  }

  @Override
  protected void processProperties(
      ConfigurableListableBeanFactory beanFactoryToProcess,
      Properties props) throws BeansException {
    super.processProperties(beanFactoryToProcess, props);

    if (log.isDebugEnabled()) {
      log.debug("Processing properties");
      for (Map.Entry<Object, Object> entry : props.entrySet()) {
        log.debug("{}: '{}'", entry.getKey(), entry.getValue());
      }
    }
  }

  @Override
  protected String resolvePlaceholder(String placeholder, Properties props) {
    String value = super.resolvePlaceholder(placeholder, props);

    if (value == null) {
      log.error("Placeholder '{}' couldn't be resolved", placeholder);
      if (log.isDebugEnabled()) {
        log.debug("  properties:");
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
          log.debug("    {}: '{}'", entry.getKey(), entry.getValue());
        }
      }
    }
    return value;
  }

}
