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
import org.bedework.webcache.common.Configuration;
import org.bedework.webcache.common.WebCacheUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author douglm
 *
 */
public abstract class MethodBase implements Logged {
  protected WebCacheUtil util;
  protected Configuration config;

  /**
   * @throws ServletException on fatal error
   */
  public MethodBase(final Configuration config) throws ServletException {
    this.config = config;

    try {
      util = new WebCacheUtil();
    } catch (final Throwable t) {
      throw new ServletException(t);
    }
  }

  /**
   * @param req http request
   * @param resp http response
   * @throws ServletException on fatal error
   */
  public abstract void doMethod(HttpServletRequest req,
                                HttpServletResponse resp)
        throws ServletException;

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
