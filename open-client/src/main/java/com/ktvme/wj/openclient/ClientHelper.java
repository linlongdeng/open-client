package com.ktvme.wj.openclient;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClientHelper
{
  private static final Log logger = LogFactory.getLog(ClientHelper.class);

  public static String sendRequest(String url, String authenticator, String desKey, String appId, String jsonContent)
  {
    if (StringUtils.isBlank(url)) {
      throw new IllegalArgumentException("参数 url 不能为空!");
    }

    if (StringUtils.isBlank(desKey)) {
      throw new IllegalArgumentException("参数 desKey 不能为空!");
    }

    if (StringUtils.isBlank(appId)) {
      throw new IllegalArgumentException("参数 appId 不能为空!");
    }

    if (StringUtils.isBlank(jsonContent)) {
      throw new IllegalArgumentException("参数 jsonContent 不能为空!");
    }

    if (logger.isInfoEnabled()) {
      logger.info("发送http请求到锐捷WMC平台，url :[" + url + "]" + ",content :[" + jsonContent + "]");
    }
    String sign = Algorithm.md5Encrypt(jsonContent + "&" + authenticator);
    String desContent = Algorithm.TrippleEncrypt(jsonContent, desKey);

    List params = new ArrayList();
    params.add(new NameValuePair("version", "1.0"));
    params.add(new NameValuePair("content", desContent));
    params.add(new NameValuePair("sign", sign));
    params.add(new NameValuePair("appId", appId));

    String result = HttpClientUtils.doPost(url, (NameValuePair[])params.toArray(new NameValuePair[params.size()]), "utf-8");

    ResponseDto resultDto = (ResponseDto)JsonUtil.parse(result, ResponseDto.class);

    if (resultDto.getSign() != null) {
      String content = Algorithm.TrippleDecrypt(resultDto.getContent(), desKey);
      sign = Algorithm.md5Encrypt(content + "&" + authenticator);
      if (resultDto.getSign().equals(sign)) {
        CodeDto codeDto = (CodeDto)JsonUtil.parse(content, CodeDto.class);
        if (100 == codeDto.getCode()) {
          logger.info("wmc open api 接口调用成功，返回的json结果为: " + content);
        }
        else
          logger.error("wmc open api 接口调用失败,失败原因: " + codeDto.getDesc());
      }
      else
      {
        logger.error("wmc open api 返回数据被篡改了!");
      }
      return content;
    }

    String content = resultDto.getContent();
    CodeDto codeDto = (CodeDto)JsonUtil.parse(content, CodeDto.class);
    if (100 == codeDto.getCode()) {
      logger.info("wmc open api 接口调用成功，返回的json结果为: " + content);
    }
    else {
      logger.error("wmc open api 接口调用失败,失败原因: " + codeDto.getDesc());
    }
    return content;
  }
}