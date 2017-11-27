package com.whu.yves.protocal.http;

import com.whu.yves.protocal.Parser;

public class ResponseParser extends Parser {
  private int statusCode;
  private int contentLength;
  private String contentType;
  private int headerLength;
  public ResponseParser(String message) {
    super(message);
    headerLength = message.length();
  }

  @Override
  public boolean check() {
    return true;
  }

  @Override
  public void parse() {
    String[] parms = message.split("\n");

    // 解析statusCode;
    int index1 = parms[0].indexOf(' ');
    if (index1 != -1) {
      int index2 = parms[0].indexOf(' ', index1 + 1);
      if (index2 > index1) {
        statusCode = Integer.parseInt(parms[0].substring(index1 + 1, index2));
      }
    }

    for (String line : parms) {
      if (line.startsWith("Content-Length")) {
        contentLength = Integer.valueOf(line.split(" ")[1]);
      } else if (line.startsWith("Content-Type")) {
        contentType = line.split(" ")[1];
      }
    }
  }

  public int getStatusCode() {
    return statusCode;
  }

  public int getContentLength() {
    return contentLength;
  }

  public int getHeaderLength() {
    return headerLength;
  }

  public String getContentType() {
    return contentType;
  }

}
