/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.webcache.common;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.net.http.HttpRequest;
import java.time.Duration;

/**
 * User: mike Date: 3/11/22 Time: 23:06
 */
public class Client {
  static CloseableHttpClient client;

  public static HttpRequest.Builder getRequestNuilder() {
    return HttpRequest.newBuilder()
                      .timeout(Duration.ofSeconds(30));
  }

  public static synchronized CloseableHttpClient getClient() {
    if(client!= null) {
      return client;
    }

    final PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();

    connManager.setMaxTotal(100);

    final HttpClientBuilder clientbuilder = HttpClients.custom().setConnectionManager(connManager);

    client = clientbuilder.
            disableRedirectHandling().
            build();

    return client;
  }
}
