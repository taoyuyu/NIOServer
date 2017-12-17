package com.whu.yves.server.load;

import java.util.ArrayList;
import java.util.Random;

public class RandomLoadBalance extends DefaultLoadBalance {
  private Random random = new Random();
  public RandomLoadBalance(ArrayList<String> hosts) {
    super(hosts);
  }

  @Override
  public String getNextHost() {
    int size = hosts.size();
    if (size == 0) {
      return null;
    }
    return hosts.get(random.nextInt(size));
  }

  @Override
  public void addOneHost(String host) {

  }
}
