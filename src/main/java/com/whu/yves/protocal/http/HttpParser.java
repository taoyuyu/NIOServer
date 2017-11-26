package com.whu.yves.protocal.http;

public class HttpParser {
  private String message = null;
  private String uri = null;
  private String method = null;
  public HttpParser(String message) {
    this.message = message;
    if (!check()) {
      throw new RuntimeException("message is not fit with http protocal");
    }
    parseMethod();
    parseUri();
  }

  private boolean check() {
    return true;
  }

  private void parseUri() {

  }

  private void parseMethod() {

  }

  public String getUri() {
    return uri;
  }

  public String getMethod() {
    return method;
  }

}
