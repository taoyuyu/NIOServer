package com.whu.yves.server.load;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.junit.Test;

public class TestLoadBalance {
  private static Logger LOG = Logger.getLogger(TestLoadBalance.class);
  @Test
  public void testLoadBalacne() {
    ArrayList<String> hosts = new ArrayList<>();
    hosts.add("localhost:8080");
    hosts.add("ten1:8080");
    hosts.add("ten2:8080");

    LoadBalance loadBalance = new PollingLoadBalance(hosts);
    for (int i=0; i<100; ++i) {
      LOG.info(loadBalance.getNextHost());
    }

  }

}
