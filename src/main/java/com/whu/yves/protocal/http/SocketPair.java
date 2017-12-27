package com.whu.yves.protocal.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.apache.commons.codec.Charsets;
import org.apache.log4j.Logger;

public class SocketPair {

  private static Logger LOG = Logger.getLogger(SocketPair.class);
  private SocketChannel channel;
  private Socket server = null;
  private String host = null;

  public SocketPair(SocketChannel channel) {
    this.channel = channel;
  }

  public boolean connect(String host) {
    if (null == host) {
      return false;
    }
    this.host = host;
    String address;
    int port;
    String[] parms = host.split(":");
    if (parms.length == 2) {
      port = Integer.valueOf(parms[1]);
    } else {
      port = 80;
    }
    address = parms[0];
    try {
      server = new Socket(address, port);
      LOG.info(String.format("connect to %s succeed", host));
    } catch (IOException ioe) {
      LOG.error(String.format("connect to %s error", host));
      return false;
    }
    return true;
  }

  public void closeServerConnection() {
    if (server != null) {
      try {
        server.close();
      } catch (IOException ioe) {
        LOG.error(String.format("close connection to %s error", host));
      }
    }
  }

  public boolean deliverRequest(String request) {
    InputStreamReader input = null;
    BufferedReader br = null;
    OutputStreamWriter out = null;
    BufferedWriter bw = null;

    try {
      //向后台服务器组发送数据
      out = new OutputStreamWriter(server.getOutputStream(), Charsets.UTF_8);
      bw = new BufferedWriter(out);
      bw.write(request);
      bw.flush();

      //读取返回的数据
      DataInputStream dis = new DataInputStream(server.getInputStream());
      byte[] data = new byte[128];
      int length;
      while ((length = dis.read(data)) != -1) {
        channel.write(ByteBuffer.wrap(data));
        if (length != 128) {
          break;
        }
      }
      channel.close();
      return true;
    } catch (IOException ioe) {

    } finally {
      try {
        if (bw != null) {
          bw.close();
        }
        if (out != null) {
          out.close();
        }

        if (br != null) {
          br.close();
        }
        if (input != null) {
          input.close();
        }
        if (server != null) {
          server.close();
        }
      } catch (IOException ioe) {

      }
    }
    return true;
  }

}
