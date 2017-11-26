package com.whu.yves.protocal.http;

/**
* 打包http response
* */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class HttpPackager {
  private int status;
  private String msg;
  private int length;
  private String content;

  private static String WEB_ROOT = System.getProperty("user.dir") + File.separator + "static";
  private static String template = "HTTP/1.1 %d %s\r\n" +
      "Content-Type: text/html\r\n" +
      "Content-Length: %d\r\n" +
      "\r\n" +
      "%s";

  public HttpPackager(HttpParser parser) {
    status = 200;
    msg = "OK";
    if ("/".equals(parser.getUri())) {
      content = readFile(new File(WEB_ROOT+File.separator+"index.html"));;
    } else {
      File file = new File(WEB_ROOT, parser.getUri());
      if (file.exists()) {
        content = readFile(file);
      } else {
        status = 404;
        msg = "File Not Found";
        content = readFile(new File(WEB_ROOT+File.separator+"404.html"));
      }
    }

    length = content.length();
  }

  public String getResponse() {
    return String.format(template, status, msg, length, content);
  }

  private String readFile(File file) {
    StringBuilder sb = new StringBuilder();
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(file));
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
        }
      }
    }
    return sb.toString();
  }
}
