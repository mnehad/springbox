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

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.json.MetricsModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

@Controller
public class MetricsController implements InitializingBean {

  @Autowired
  private MetricRegistry registry;

  private TimeUnit rateUnit = TimeUnit.SECONDS;
  private TimeUnit durationUnit = TimeUnit.SECONDS;
  private boolean showSamples = true;
  private ObjectMapper mapper;

  public void setRateUnit(TimeUnit rateUnit) {
    Assert.notNull(rateUnit, "rateUnit must be set!");
    this.rateUnit = rateUnit;
  }

  public void setDurationUnit(TimeUnit durationUnit) {
    Assert.notNull(rateUnit, "durationUnit must be set!");
    this.durationUnit = durationUnit;
  }

  public void setShowSamples(boolean showSamples) {
    this.showSamples = showSamples;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    mapper = new ObjectMapper().registerModule(new MetricsModule(rateUnit, durationUnit, showSamples));
  }

  @RequestMapping(value = "/metrics/metrics", method = RequestMethod.GET)
  public void process(@RequestParam(value="pretty", required = false, defaultValue = "true") boolean prettyPrint,
                      HttpServletResponse resp) throws IOException {
    resp.setContentType("application/json");
    resp.setHeader("Cache-Control", "must-revalidate,no-cache,no-store");
    resp.setStatus(HttpServletResponse.SC_OK);

    final OutputStream output = resp.getOutputStream();
    try {
      (prettyPrint ? mapper.writerWithDefaultPrettyPrinter() : mapper.writer()).writeValue(output, registry);
    } finally {
      output.close();
    }
  }

}
