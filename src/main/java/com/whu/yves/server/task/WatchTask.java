package com.whu.yves.server.task;

import com.whu.yves.protocal.http.HttpProxy;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;

public class WatchTask implements Runnable {
  private String host;
  private static Logger LOG = Logger.getLogger(WatchTask.class);
  public WatchTask(String host) {
    this.host = host;
  }

  @Override
  public void run() {

    String address;
    int port;
    String[] parms = host.split(":");
    if (parms.length == 2) {
      port = Integer.valueOf(parms[1]);
    } else {
      port = 80;
    }
    address = parms[0];
    Socket server;
    try {
      server = new Socket(address, port);
      server.close();
      LOG.info(String.format("connect to %s succeed", host));
      HttpProxy.loadBalance.addOneHost(host);
      Future future = HttpProxy.futureTasks.get(host);
      if (future != null) {
        HttpProxy.futureTasks.remove(host);
        future.cancel(false);
        LOG.info("WatchTask cancel succeed");
      }
    } catch (IOException ioe) {

    }
  }
}