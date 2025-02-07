/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.webcache.common;

import org.bedework.util.logging.BwLogger;
import org.bedework.util.logging.Logged;

import jakarta.servlet.http.HttpServletRequest;

/**
 * User: mike Date: 3/11/22 Time: 17:03
 */
public class WebCacheUtil implements Logged {
  /** Build a request for the target from the incoming request
   *
   * @param req incoming request
   * @return target request - null for invalid incoming request
   */
  public static HttpServletRequest buildRequest(final HttpServletRequest req) {
    return null;
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
