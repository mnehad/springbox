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
package org.vbossica.springbox.metrics;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.json.HealthCheckModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.SortedMap;

import static javax.servlet.http.HttpServletResponse.*;

@Controller
public class HealthCheckController implements InitializingBean {

  @Autowired
  private HealthCheckRegistry registry;
  private ObjectMapper mapper;

  @Override
  public void afterPropertiesSet() throws Exception {
    mapper = new ObjectMapper().registerModule(new HealthCheckModule());
  }

  @RequestMapping(value = "/metrics/healthcheck", method = RequestMethod.GET)
  public void process(@RequestParam(value="pretty", required = false, defaultValue = "true") boolean prettyPrint,
                      HttpServletResponse resp) throws IOException {
    SortedMap<String, HealthCheck.Result> results = registry.runHealthChecks();
    resp.setContentType("application/json");
    resp.setHeader("Cache-Control", "must-revalidate,no-cache,no-store");
    if (results.isEmpty()) {
      resp.setStatus(SC_NOT_IMPLEMENTED);
    } else {
      resp.setStatus(isAllHealthy(results) ? SC_OK : SC_INTERNAL_SERVER_ERROR);
    }

    OutputStream output = resp.getOutputStream();
    try {
      (prettyPrint ? mapper.writerWithDefaultPrettyPrinter() : mapper.writer()).writeValue(output, results);
    } finally {
      output.close();
    }
  }

  private boolean isAllHealthy(Map<String, HealthCheck.Result> results) {
    for (HealthCheck.Result result : results.values()) {
      if (!result.isHealthy()) {
        return false;
      }
    }
    return true;
  }

}