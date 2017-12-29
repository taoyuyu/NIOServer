package com.whu.yves.server.load;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class DefaultLoadBalance implements LoadBalance {

  protected ArrayList<String> hosts;
  protected static ReentrantReadWriteLock rwLock;

  public DefaultLoadBalance(ArrayList<String> hosts, boolean isFair) {
    this.hosts = hosts;
    //true -> 公平锁; false -> 写优先
    rwLock = new ReentrantReadWriteLock(isFair);
  }

  @Override
  public boolean removeOneHost(String host) {
    boolean result = false;
    if (hosts.contains(host)) {
      rwLock.writeLock().lock();
      try {
        result = hosts.remove(host);
      } finally {
        rwLock.writeLock().unlock();
      }
    }
    return result;
  }

  @Override
  public void addOneHost(String host) {
    if (!hosts.contains(host)) {
      rwLock.writeLock().lock();
      try {
        hosts.add(host);
      } finally {
        rwLock.writeLock().unlock();
      }
    }
  }

}
