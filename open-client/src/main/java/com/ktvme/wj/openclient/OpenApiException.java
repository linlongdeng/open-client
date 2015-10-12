package com.ktvme.wj.openclient;

public class OpenApiException extends RuntimeException
{
  private static final long serialVersionUID = 4006001301589380067L;

  public OpenApiException()
  {
  }

  public OpenApiException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public OpenApiException(String message) {
    super(message);
  }

  public OpenApiException(Throwable cause) {
    super(cause);
  }
}