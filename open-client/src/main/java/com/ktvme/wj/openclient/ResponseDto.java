package com.ktvme.wj.openclient;

public class ResponseDto
{
  private String content;
  private String sign;

  public String getContent()
  {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getSign() {
    return this.sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public String toString()
  {
    return "{ content: " + getContent() + 
      " sign: " + getSign() + " }";
  }
}