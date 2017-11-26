package com.whu.yves.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import org.apache.log4j.Logger;

public class HttpDeliverer {
  private Logger LOG = Logger.getLogger(HttpDeliverer.class);
  private String request;
  public HttpDeliverer(String request) {
    this.request = request;
  }

  public String deliver(String host) throws IOException {
    LOG.info("host: " + host);
    LOG.info("deliver request: " + request);
    Socket socket = null;
    String[] parms = host.split(":");
    try {
      socket = new Socket(parms[0], Integer.valueOf(parms[1]));
      //向后台服务器组发送数据
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
      out.write(request.getBytes());
      out.write("\r\n".getBytes());
      //读取返回的数据
      DataInputStream input = new DataInputStream(socket.getInputStream());
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line=input.readLine())!=null) {
        sb.append(line+"\r\n");
      }
      return sb.toString();
    } finally {
      socket.close();
    }
  }

}
