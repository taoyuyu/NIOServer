package com.whu.yves.protocal.http;


import com.whu.yves.configuration.reader.YamlReader;
import com.whu.yves.protocal.UtilStrings;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class HttpProxy {
  private static Logger LOG = Logger.getLogger(HttpProxy.class);

  private String request = null;
  private static String WEB_ROOT = System.getProperty("user.dir") + File.separator + "static";
  private static String template = "HTTP/1.1 %d %s\n" +
      "Content-Type: text/html\n" +
      "Content-Length: %d\n" +
      "\n" +
      "%s";
  private String content = "";

  public HttpProxy(RequestParser parser, SocketChannel channel) throws IOException {
    this.request = parser.getRequest();
    String uri = parser.getUri();
    if ("/".equals(uri)) {
      LOG.info("get index.html");
      localResource(UtilStrings.INDEX_PAGE, channel);
    } else {
      if (!deliverToRemoteHost(channel)) {
        localResource(UtilStrings.ERROR_PAGE, channel);
      }
    }
  }

  private void localResource(String url, SocketChannel channel) throws IOException {
    readFile(url);
    int status = 200;
    String msg = "OK";
    int length = content.length();
    channel.write(ByteBuffer.wrap(String.format(template, status, msg, length, content).getBytes()));
    channel.close();
  }

  private boolean deliverToRemoteHost(SocketChannel channel) {
    ArrayList<String> hosts = YamlReader.getHosts();
    SocketPair socketPair = new SocketPair(channel);
    for (String host : hosts) {
      if (socketPair.connect(host)) {
        if (socketPair.deliverRequest(request)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean readFile(String uri) {
    String filePath = WEB_ROOT+File.separator + uri;
    File file = new File(filePath);
    StringBuilder sb = new StringBuilder();
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(file));
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
    } catch (IOException e) {
      LOG.error(String.format("File %s%s not found", filePath));
      return false;
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
          return false;
        }
      }
    }
    content = sb.toString();
    return true;
  }
}
