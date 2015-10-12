package com.ktvme.wj.openclient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpClientUtils
{
  private static final Log log = LogFactory.getLog(HttpConnectionManager.class);

  private static int timeOut = 3000;

  private static int retryCount = 1;

  private static int connectionTimeout = 3000;

  private static int maxHostConnections = 200;

  private static int maxTotalConnections = 400;

  private static HttpClient httpClient = null;

  private static String executeMethod(HttpClient httpClient, HttpMethod method, String charsetName)
  {
    StopWatch watch = new StopWatch();
    int status = -1;
    try {
      if (log.isDebugEnabled()) {
        log.debug("Execute method(" + method.getURI() + ") begin...");
      }
      watch.start();
      status = httpClient.executeMethod(method);
      watch.stop();

      if (status == 200) {
        InputStream inputStream = method.getResponseBodyAsStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, baos);
        String response = new String(baos.toByteArray(), charsetName);
        if (log.isInfoEnabled()) {
          log.info("Response is: " + response);
        }
        String str1 = response;
        return str1;
      }

      String errorMsg = "Http request failure! status is {}" + status;
      log.error(errorMsg);
      return "{\"content\":\"{'code':'107','desc':'错误:系统错误'}\"}";
    }
    catch (SocketTimeoutException e1) {
      log.error("Request time out!");
      return "{\"content\":\"{'code':'106','desc':'错误:服务调用超时'}\"}";
    } catch (ConnectException e2) {
      log.error("ConnectException refused!");
      return "{\"content\":\"{'code':'109','desc':'错误:网络连接服务端失败'}\"}";
    } catch (Exception e3) {
      log.error("Error occur!", e3);
      return "{\"content\":\"{'code':'107','desc':'错误:系统错误'}\"}";
    } finally {
      method.releaseConnection();
      if (log.isDebugEnabled())
        log.debug("Method " + method.getName() + ",statusCode " + status + ",consuming " + watch.getTime() + 
          " ms");
    }
   // throw localObject;
  }

  private static PostMethod createPostMethod(String uri, NameValuePair[] params, String charsetName)
  {
    PostMethod method = new PostMethod(uri);
    method.addParameters(params);
    method.getParams().setContentCharset(charsetName);
    return method;
  }

  private static HttpClient createHttpClient()
  {
    HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());

    HttpConnectionManagerParams httpConnectionManagerParams = httpClient.getHttpConnectionManager().getParams();
    httpConnectionManagerParams.setConnectionTimeout(connectionTimeout);
    httpConnectionManagerParams.setTcpNoDelay(true);
    httpConnectionManagerParams.setSoTimeout(timeOut);
    httpConnectionManagerParams.setDefaultMaxConnectionsPerHost(maxHostConnections);
    httpConnectionManagerParams.setMaxTotalConnections(maxTotalConnections);

    HttpClientParams httpClientParam = httpClient.getParams();

    httpClientParam.setParameter("http.method.retry-handler", 
      new DefaultHttpMethodRetryHandler(retryCount, 
      false));
    httpClientParam.setCookiePolicy("compatibility");

    return httpClient;
  }

  public static String doPost(String url, NameValuePair[] params, String charsetName) {
    return executeMethod(getHttpClient(), createPostMethod(url, params, charsetName), charsetName);
  }

  private static HttpClient getHttpClient()
  {
    if (httpClient != null) {
      return httpClient;
    }
    httpClient = createHttpClient();
    return httpClient;
  }
}