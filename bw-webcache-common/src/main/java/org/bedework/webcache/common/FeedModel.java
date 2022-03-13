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
  public static URI getUri(final ParsedRequest preq,
                           final Configuration config) {
    switch (preq.getAction()) {
      case "icsDays": {
        return buildIcsDays(preq, config);
      }
      case "jsonDays": {
        return buildJsonDays(preq, config);
      }
      case "rssDays": {
        return buildRssDays(preq, config);
      }
      default:
        throw new RuntimeException("Unsupported action: " + preq.getAction());
    }
  }

  public static String getContentType(final ParsedRequest preq) {
    switch (preq.getAction()) {
      case "icsDays": {
        return "text/calendar; charset=UTF-8";
      }
      case "jsonDays": {
        return "application/javascript; charset=UTF-8";
      }
      case "rssDays": {
        return "application/rss+xml; charset=UTF-8";
      }
      default:
        throw new RuntimeException("Unsupported action: " + preq.getAction());
    }
  }

  public static URI buildIcsDays(final ParsedRequest preq,
                                 final Configuration config) {
    try {
      final URIBuilder bldr = getCommon(preq, config);

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
      final URIBuilder bldr = getCommon(preq, config);

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
      final URIBuilder bldr = getCommon(preq, config);

      bldr.addParameter("skinName",
                        getSkin(preq.getSkin()));

      return bldr.build();
    } catch (final Throwable t) {
      throw new RuntimeException(t);
    }
  }

  private static URIBuilder getCommon(final ParsedRequest preq,
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
