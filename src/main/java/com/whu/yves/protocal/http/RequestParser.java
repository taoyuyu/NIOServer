package com.whu.yves.protocal.http;

import com.whu.yves.protocal.Parser;

public class RequestParser extends Parser {
  private String uri = null;
  private String method = null;
  public RequestParser(String message) {
    super(message);
  }

  @Override
  public void parse() {
    parseMethod();
    parseUri();
  }

  @Override
  public boolean check() {
    return true;
  }

  private void parseUri() {
    int index1, index2;
    index1 = message.indexOf(' ');
    if (index1 != -1) {
      index2 = message.indexOf(' ', index1 + 1);
      if (index2 > index1)
        uri = message.substring(index1 + 1, index2);
    }
  }

  private void parseMethod() {
    if (message.startsWith("GET")) {
      method = "GET";
    } else if (message.startsWith("POST")) {
      method = "POST";
    }
  }

  public String getUri() {
    return uri;
  }

  public String getRequest() {
    return this.message;
  }

  public String getMethod() {
    return method;
  }

}
