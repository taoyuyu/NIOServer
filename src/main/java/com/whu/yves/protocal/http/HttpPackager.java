package com.whu.yves.protocal.http;

/**
* 打包http response
* */
import com.whu.yves.configuration.reader.YamlReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class HttpPackager {
  private static Logger LOG = Logger.getLogger(HttpDeliverer.class);
  private String request = null;
  private String response = null;
  private static String WEB_ROOT = System.getProperty("user.dir") + File.separator + "static";
  private static String template = "HTTP/1.1 %d %s\n" +
      "Content-Type: text/html\n" +
      "Content-Length: %d\n" +
      "\n" +
      "%s";

  public HttpPackager(RequestParser parser) {
    this.request = parser.getRequest()+"\n";
    int status = 200;
    String msg = "OK";
    int length;
    String content;

    if ("/".equals(parser.getUri())) {
      content = readFile(new File(WEB_ROOT+File.separator+"index.html"));
    } else {
      File file = new File(WEB_ROOT, parser.getUri());
      if (file.exists()) {
        content = readFile(file);
      } else {
        if (deliverToRemoteServer() == 200) {
          return;
        }
        status = 404;
        msg = "File Not Found";
        content = readFile(new File(WEB_ROOT+File.separator+"404.html"));
      }
    }
    length = content.length();
    response = String.format(template, status, msg, length, content);
  }

  private int deliverToRemoteServer() {
    ArrayList<String> hosts = YamlReader.getHosts();
    HttpDeliverer deliverer = new HttpDeliverer(this.request);
    for (String host : hosts) {
      try {
        response = deliverer.deliver(host);
        int status = deliverer.getStatusCode();
        LOG.info(String.format("response from host: %s => %s", host, response));
        return status;
      } catch (IOException ioe) {

      }
    }
    return 500;
  }


  public String getResponse() {
    return response;
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
