/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.webcache.common;

import org.bedework.util.misc.ToString;

import javax.servlet.http.HttpServletRequest;

/**
 * User: mike Date: 3/11/22 Time: 22:18
 */
public class ParsedRequest {
  boolean valid;

  int days;
  String skin;
  String objname;
  String filter;

  public ParsedRequest(final HttpServletRequest req) {
    try {
      final String path = req.getPathInfo();

      if ((path == null) || !path.startsWith("/v1.0/")) {
        return;
      }

      final String[] pathEls = path.substring(1).split("/");
      if (pathEls.length < 2) {
        return;
      }

      switch (pathEls[1]) {
        case "icsDays": {
          if (pathEls.length != 5) {
            return;
          }

          days = Integer.parseInt(pathEls[2]);
          filter = removeSuffix(pathEls[3], ".ics");
          if (filter == null) {
            return;
          }

          break;
        }
        case "jsonDays": {
          if (pathEls.length != 6) {
            return;
          }

          days = Integer.parseInt(pathEls[2]);
          skin = pathEls[3];
          filter = pathEls[4];
          objname = removeSuffix(pathEls[5], ".json");
          if ((skin == null) || (filter == null) || (objname == null)) {
            return;
          }

          break;
        }
        case "rssDays": {
          if (pathEls.length != 6) {
            return;
          }

          days = Integer.parseInt(pathEls[2]);
          skin = pathEls[3];
          filter = removeSuffix(pathEls[4], ".rss");
          if ((skin == null) || (filter == null)) {
            return;
          }

          break;
        }
        default:
          return;
      }

      valid = true;
    } catch (final Throwable t) {
      valid = false;
    }
  }

  public boolean isValid() {
    return valid;
  }

  public int getDays() {
    return days;
  }

  public String getSkin() {
    return skin;
  }

  public String getObjname() {
    return objname;
  }

  public String getFilter() {
    return filter;
  }

  private String removeSuffix(final String val,
                              final String suffix) {
    if (!val.endsWith(suffix)) {
      return null;
    }

    return val.substring(0, val.length() - suffix.length());
  }

  public String toString() {
    final ToString ts = new ToString(this);

    ts.append("days", days);
    ts.append("skin", skin);
    ts.append("objname", objname);
    ts.append("filter", filter);

    return ts.toString();
  }
}
