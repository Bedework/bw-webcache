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

import org.bedework.webcache.common.Configuration;
import org.bedework.webcache.common.ParsedRequest;
import org.bedework.webcache.common.Client;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Class called to handle GET.
 *
 *   @author Mike Douglass
 */
public class GetMethod extends MethodBase {
  /**
   * @throws ServletException on fatal error
   */
  public GetMethod(final Configuration config) throws ServletException {
    super(config);
  }

  @Override
  public void doMethod(final HttpServletRequest req,
                       final HttpServletResponse resp) throws ServletException {
    final ParsedRequest preq = new ParsedRequest(req);
    if (!preq.isValid()) {
      resp.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
      return;
    }

    if (debug()) {
      debug("GetMethod: doMethod  preq=" + preq.toString());
    }

    try {
      final Client theTarget = new Client(config.getTarget());
    } catch (final Throwable t){
      resp.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
      return;
    }
  }

  /* Return true if data unchanged - status is set * /
  private boolean ifNoneMatchTest(final HttpServletRequest req,
                                  final HttpServletResponse resp) throws ServletException {
    try {
      final String inEtag = req.getHeader("If-None-Match");

      if (inEtag == null) {
        return false;
      }

      if (!inEtag.equals(util.getEtag())) {
        return false;
      }

      resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      return true;
    } catch (final Throwable t) {
      throw new ServletException(t);
    }
  } */
}
