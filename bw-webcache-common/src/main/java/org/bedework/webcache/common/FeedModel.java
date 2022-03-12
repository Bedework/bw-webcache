/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.webcache.common;

import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * User: mike Date: 3/11/22 Time: 22:00
 */
public class FeedModel {
  public static URI buildIcsDays(final ParsedRequest preq,
                                 final Configuration config) {
    try {
      final URIBuilder bldr = getUri(preq, config);

      bldr.addParameter("format",
                        "text/calendar");

      return bldr.build();
    } catch (final Throwable t) {
      throw new RuntimeException(t);
    }
  }

  public static URI buildJsonDays(final ParsedRequest preq,
                                  final Configuration config) {
    try {
      final URIBuilder bldr = getUri(preq, config);

      if (!"no--object".equals(preq.getObjname())) {
        bldr.addParameter("setappvar",
                          "objName(" + preq.getObjname() + ")");
      }

      bldr.addParameter("skinName",
                        getSkin(preq.getSkin()));

      return bldr.build();
    } catch (final Throwable t) {
      throw new RuntimeException(t);
    }
  }

  public static URI buildRssDays(final ParsedRequest preq,
                                  final Configuration config) {
    try {
      final URIBuilder bldr = getUri(preq, config);

      bldr.addParameter("skinName",
                        getSkin(preq.getSkin()));

      return bldr.build();
    } catch (final Throwable t) {
      throw new RuntimeException(t);
    }
  }

  private static URIBuilder getUri(final ParsedRequest preq,
                                   final Configuration config) {
    try {
      final URIBuilder bldr = new URIBuilder(
              config.getTarget() +
                      config.getTargetContext() +
                      "/main/listEvents.do")
              .addParameter("calPath", "/public/cals/MainCal")
              .addParameter("setappvar",
                            "summaryMode(details)");

      if (!"no--filter".equals(preq.getFilter())) {
        bldr.addParameter("fexpr",
                          URLEncoder.encode(preq.getFilter(),
                                            StandardCharsets.UTF_8.toString()));
      }

      if (preq.getDays() != 0) {
        bldr.addParameter("days",
                          String.valueOf(preq.getDays()));
      }

      return bldr;
    } catch (final Throwable t) {
      throw new RuntimeException(t);
    }
  }

  private static String getSkin(final String key) {
    if ("list-xml".equals(key)) {
      return "default";
    }

    return key;
  }

}
