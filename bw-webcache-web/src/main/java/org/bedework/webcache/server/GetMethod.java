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

import org.bedework.webcache.common.Client;
import org.bedework.webcache.common.Configuration;
import org.bedework.webcache.common.FeedModel;
import org.bedework.webcache.common.ParsedRequest;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;

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
      final HttpGet get = new HttpGet(FeedModel.getUri(preq, config));
      get.addHeader("content-type", FeedModel.getContentType(preq));

      try (final CloseableHttpResponse targetResp = Client.getClient()
                                                    .execute(get)) {
        final StatusLine status = targetResp.getStatusLine();
        if ((status.getStatusCode() / 100) != 2) {
          resp.setStatus(status.getStatusCode());
          return;
        }

        final Header ctype = targetResp.getFirstHeader("content-type");

        if (ctype == null) {
          resp.setHeader("content-type",
                         FeedModel.getContentType(preq));
        } else {
          resp.setHeader("content-type", ctype.getValue());
        }

        final HttpEntity targetEntity = targetResp.getEntity();
        resp.setContentLengthLong(targetEntity.getContentLength());

        try (final InputStream targetStream = targetEntity.getContent()) {
          final BufferedInputStream bis = new BufferedInputStream(targetStream);
          final BufferedOutputStream bos = new BufferedOutputStream(resp.getOutputStream());

          int i;
          while ((i = bis.read()) != -1) {
            bos.write(i);
          }

          bos.flush();
          bos.close();
          bis.close();
        }
      }
      resp.setStatus(HttpServletResponse.SC_OK);
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
