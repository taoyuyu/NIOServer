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
    DataInputStream input = null;
    DataOutputStream out = null;

    String[] parms = host.split(":");
    try {
      socket = new Socket(parms[0], Integer.valueOf(parms[1]));
      if (!socket.isConnected()) {
        LOG.info("connect failed");
        throw new IOException();
      }
      //向后台服务器组发送数据
      out = new DataOutputStream(socket.getOutputStream());
      out.write(request.getBytes());

      //读取返回的数据
      input = new DataInputStream(socket.getInputStream());
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line=input.readLine())!=null) {
        sb.append(line+"\n");
      }
      return sb.toString();
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

}
