/* ********************************************************************
    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License. You may obtain a
    copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on
    an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied. See the License for the
    specific language governing permissions and limitations
    under the License.
*/
package org.bedework.webcache.server;

import org.bedework.util.logging.BwLogger;
import org.bedework.util.logging.Logged;
import org.bedework.util.servlet.HttpServletUtils;
import org.bedework.webcache.common.Configuration;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/** Serve up timezone information. This server is an intermediate step towards
 * a real global timezone service being promoted in CalConnect
 *
 * <p>This server provides timezones stored in a zip file at a given remote
 * location accessible over http. This allows easy replacement of timezone
 * definitions.
 *
 * @author Mike Douglass
 *
 */
public class

WebCacheServer extends HttpServlet
        implements Logged {
  protected boolean dumpContent;
  private Configuration configuration = new Configuration();

  @Override
  public void init(final ServletConfig config) throws ServletException {
    try {
      super.init(config);

      dumpContent = "true".equals(config.getInitParameter("dumpContent"));
      configuration.setTarget(config.getInitParameter("target"));
      configuration.setTargetContext(config.getInitParameter("targetContext"));
    } catch (final ServletException se) {
      throw se;
    } catch (final Throwable t) {
      throw new ServletException(t);
    }
  }

  @Override
  protected void service(final HttpServletRequest req,
                         final HttpServletResponse resp) throws ServletException, IOException {
    try {
      final String methodName = req.getMethod();

      if (debug()) {
        debug("entry: " + methodName);
        dumpRequest(req);
      }

      if ("GET".equals(methodName)) {
        new GetMethod(configuration).doMethod(req, resp);
      } else {
        resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
      }
    } finally {
      /* We're stateless - toss away any session */
      try {
        final HttpSession sess = req.getSession(false);
        if (sess != null) {
          sess.invalidate();
        }
      } catch (final Throwable ignored) {}
    }
  }

  /** Debug
   *
   * @param req http request
   */
  @SuppressWarnings("unchecked")
  public void dumpRequest(final HttpServletRequest req) {
    try {
      String title = "Request headers";

      debug(title);

      HttpServletUtils.dumpHeaders(req);

      final Enumeration<String> names = req.getParameterNames();

      title = "Request parameters";

      debug(title + " - global info and uris");
      debug("getRemoteAddr = " + req.getRemoteAddr());
      debug("getRequestURI = " + req.getRequestURI());
      debug("getRemoteUser = " + req.getRemoteUser());
      debug("getRequestedSessionId = " + req.getRequestedSessionId());
      debug("HttpUtils.getRequestURL(req) = " + req.getRequestURL());
      debug("contextPath=" + req.getContextPath());
      debug("query=" + req.getQueryString());
      debug("contentlen=" + req.getContentLength());
      debug("request=" + req);
      debug("parameters:");

      debug(title);

      while (names.hasMoreElements()) {
        final String key = names.nextElement();
        final String val = req.getParameter(key);
        debug("  " + key + " = \"" + val + "\"");
      }
    } catch (final Throwable ignored) {
    }
  }

  /* ====================================================================
   *                   Logged methods
   * ==================================================================== */

  private final BwLogger logger = new BwLogger();

  @Override
  public BwLogger getLogger() {
    if ((logger.getLoggedClass() == null) && (logger.getLoggedName() == null)) {
      logger.setLoggedClass(getClass());
    }

    return logger;
  }
}
