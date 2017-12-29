package com.whu.yves.server.load;

import java.util.ArrayList;

public class WeightLoadBalance extends DefaultLoadBalance {
  public WeightLoadBalance(ArrayList<String> hosts) {
    super(hosts, false);
  }

  @Override
  public String getNextHost() {
    //TODO 根据权重的负载均衡
    return null;
  }

}
