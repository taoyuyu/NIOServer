package com.whu.yves.protocal.http;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import org.apache.log4j.Logger;

public class HttpDeliverer {

  private Logger LOG = Logger.getLogger(HttpDeliverer.class);
  private String request;
  private String response;
  private ResponseParser parser;

  public HttpDeliverer(String request) {
    this.request = request;
  }

  public String deliver(String host) throws IOException {
    LOG.info("host: " + host);
    LOG.info("deliver request: " + request);
    Socket socket = null;
    DataInputStream input = null;
    DataOutputStream out = null;

    String[] parms = host.split(":");
    try {
      socket = new Socket(parms[0], Integer.valueOf(parms[1]));
      //向后台服务器组发送数据
      out = new DataOutputStream(socket.getOutputStream());
      out.write(request.getBytes());

      //读取返回的数据
      input = new DataInputStream(socket.getInputStream());
      StringBuilder sb = new StringBuilder();
      String line;
      boolean tag = false;
      while ((line = input.readLine()) != null) {
        sb.append(line);
        sb.append("\n");
        if (!tag && line.equals("")) {
          tag = true;
          parser = new ResponseParser(sb.toString());
          parser.parse();
          if (parser.getStatusCode() != 200) {
            break;
          }
        }

        if (parser != null && sb.length() - parser.getHeaderLength() >= parser.getContentLength()) {
          break;
        }
      }
      response = sb.toString();
      return response;
    } finally {
      if (out != null) {
        out.close();
      }
      if (input != null) {
        input.close();
      }
      if (socket != null) {
        socket.close();
      }
    }
  }

  public int getStatusCode() {
    return parser.getStatusCode();
  }
}
