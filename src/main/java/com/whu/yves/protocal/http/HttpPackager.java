package com.whu.yves.protocal.http;

/**
* 打包http response
* */

public class HttpPackager {
  private HttpParser parser = null;
  private int status;
  private String msg;
  private int length;
  private String content;

  private static String template = "HTTP/1.1 %d %s\r\n" +
      "Content-Type: text/html\r\n" +
      "Content-Length: %d\r\n" +
      "\r\n" +
      "%s";

  public HttpPackager(HttpParser parser) {
    this.parser = parser;
    String uri = parser.getUri();
    if (uri.equals("/")) {
      status = 200;
      msg = "OK";
      content = "<h1 align=\"center\">Welcome to NIOServer</h1>";

    } else {
      status = 404;
      msg = "File Not Found";
      content = "<h1 align=\"center\">Error: File Not Found</h1>";
    }
    length = content.length();
  }

  public String getResponse() {
    return String.format(template, status, msg, length, content);
  }
}
