package com.whu.yves.protocal.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.apache.commons.codec.Charsets;
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
    InputStreamReader input = null;
    BufferedReader br = null;
    OutputStreamWriter out = null;
    BufferedWriter bw = null;

    String[] parms = host.split(":");
    try {
      socket = new Socket(parms[0], Integer.valueOf(parms[1]));
      //向后台服务器组发送数据
      out = new OutputStreamWriter(socket.getOutputStream(), Charsets.UTF_8);
      bw = new BufferedWriter(out);
      bw.write(request);
      bw.flush();

      //读取返回的数据
      input = new InputStreamReader(socket.getInputStream(), Charsets.UTF_8);
      br = new BufferedReader(input);
      StringBuilder sb = new StringBuilder();
      String line;
      boolean tag = false;
      while ((line = br.readLine()) != null) {
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

        if (parser != null && parser.getContentLength() > 0 && sb.length() - parser.getHeaderLength() >= parser.getContentLength()) {
          break;
        }
      }
      response = sb.toString();
      return response;
    } finally {
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
      if (socket != null) {
        socket.close();
      }
    }
  }

  public int getStatusCode() {
    return parser.getStatusCode();
  }
}
