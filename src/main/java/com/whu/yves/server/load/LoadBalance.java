package com.whu.yves.server.load;

public interface LoadBalance {
  String getNextHost();
  boolean removeOneHost(String host);
  void addOneHost(String host);
}
