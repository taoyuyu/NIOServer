package com.whu.yves.server.load;

import java.util.ArrayList;

public abstract class DefaultLoadBalance implements LoadBalance {
  protected ArrayList<String> hosts;

  public DefaultLoadBalance(ArrayList<String> hosts) {
    this.hosts = hosts;
  }

  @Override
  public boolean removeOneHost(String host) {
    return hosts.remove(host);
  }

  @Override
  public void addOneHost(String host) {
    hosts.add(host);
  }

}
