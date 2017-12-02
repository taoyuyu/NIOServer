package com.whu.yves.protocal.http;


import com.whu.yves.configuration.reader.YamlReader;
import com.whu.yves.protocal.UtilStrings;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class HttpProxy {

  private static Logger LOG = Logger.getLogger(HttpProxy.class);
  private byte[] data = null;
  private String request = null;
  private static String WEB_ROOT = System.getProperty("user.dir") + File.separator + "static";
  private static String template = "HTTP/1.1 %d %s\n" +
      "Content-Type: %s\n" +
      "Content-Length: %d\n" +
      "\n";

  public HttpProxy(RequestParser parser, SocketChannel channel) throws IOException {
    this.request = parser.getRequest();
    String uri = parser.getUri();
    if ("/".equals(uri)) {
      LOG.info("get index.html");
      localResource(UtilStrings.INDEX_PAGE, channel);
    } else {
      if (!deliverToRemoteHost(channel)) {
        localResource(uri, channel);
      }
    }
  }

  private void localResource(String uri, SocketChannel channel) throws IOException {
    LOG.info("read local resource => " + uri);
    int status = 200;
    String msg = "OK";
    String contentType = "text/html";

    if (uri.toUpperCase().endsWith(".JPEG") || uri.toUpperCase().endsWith("JPG")) {
      contentType = "image/jpeg";
    }

    if (!readFileByBytes(uri)) {
      readFileByBytes(UtilStrings.ERROR_PAGE);
      contentType = "text/html";
      status = 404;
      msg = "Not Found";
    }

    int length = data.length;
    channel
        .write(ByteBuffer.wrap(String.format(template, status, msg, contentType, length).getBytes()));
    channel.write(ByteBuffer.wrap(data));
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

  private boolean readFileByBytes(String uri) {
    String filePath = WEB_ROOT + uri;
    File file = new File(filePath);
    if (!file.exists()) {
      return false;
    }

    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
      int size = fis.available();
      data = new byte[size];
      fis.read(data);
      return true;
    } catch (IOException fnfe) {
      LOG.error(String.format("File %s not found", filePath));
      LOG.error(fnfe.getMessage());
      LOG.error(fnfe.getStackTrace());
      return false;
    } finally {
      try {
        if (fis != null) {
          fis.close();
        }
      } catch (IOException ioe) {
        return false;
      }
    }
  }
}