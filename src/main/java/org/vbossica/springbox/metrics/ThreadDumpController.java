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

import com.codahale.metrics.jvm.ThreadDump;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;

@Controller
public class ThreadDumpController implements InitializingBean {

  private ThreadDump threadDump;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.threadDump = new ThreadDump(ManagementFactory.getThreadMXBean());
  }

  @RequestMapping(value = "/metrics/threaddump", method = RequestMethod.GET)
  protected void doGet(HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/plain");
    resp.setHeader("Cache-Control", "must-revalidate,no-cache,no-store");
    resp.setStatus(HttpServletResponse.SC_OK);
    OutputStream output = resp.getOutputStream();
    try {
      threadDump.dump(output);
    } finally {
      output.close();
    }
  }

}
