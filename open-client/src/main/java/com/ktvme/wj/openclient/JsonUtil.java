package com.ktvme.wj.openclient;

import com.alibaba.fastjson.JSON;

public class JsonUtil
{
  public static String toJsonString(Object object)
  {
    return JSON.toJSONString(object);
  }

  public static <T> T parse(String jsonString, Class<T> clazz)
  {
    return JSON.parseObject(jsonString, clazz);
  }
}