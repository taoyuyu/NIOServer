package com.whu.yves.server.load;

import java.util.ArrayList;

public class PollingLoadBalance extends DefaultLoadBalance {
  private static int index = 0;
  private static int maxLength;
  public PollingLoadBalance(ArrayList<String> hosts) {
    super(hosts);
    maxLength = hosts.size();
  }

  @Override
  public synchronized String getNextHost() {
    String host = hosts.get((index++)%maxLength);
    return host;
  }
}
