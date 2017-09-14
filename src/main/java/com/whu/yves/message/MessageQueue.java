package com.whu.yves.message;

/**
 * Created by yutao on 17/9/14.
 */
public interface MessageQueue {

  String poll();

  String poll(long timeOut);

  void put(String message);

  void offer(String message);

  void offer(String message, long timeOut);
}
