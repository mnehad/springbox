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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class PingController {

  private String message = "pong";

  public void setMessage(String message) {
    this.message = message;
  }

  @RequestMapping(value = "/metrics/ping", method = RequestMethod.GET)
  public void process(HttpServletResponse resp) throws IOException {
    resp.setContentType("text/plain");
    resp.setHeader("Cache-Control", "must-revalidate,no-cache,no-store");
    resp.setStatus(HttpServletResponse.SC_OK);
    if (message != null || message.isEmpty()) {
      PrintWriter writer = resp.getWriter();
      try {
        writer.println(message);
      } finally {
        writer.close();
      }
    }
  }

}
