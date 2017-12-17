package com.whu.yves.server.load;

import java.util.ArrayList;

public class PollingLoadBalance extends DefaultLoadBalance {

  private static int index = 0;

  public PollingLoadBalance(ArrayList<String> hosts) {
    super(hosts);
  }

  @Override
  public synchronized String getNextHost() {
    int size = hosts.size();
    if (size == 0) {
      return null;
    }
    String host = hosts.get((index++) % size);
    index %= size;
    return host;
  }
}
