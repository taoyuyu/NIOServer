package com.whu.yves.message;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yutao on 17/9/14.
 */
public class MessagePool {

  private static ConcurrentHashMap<String, PushMessageQueue> messagePool = null;

  private MessagePool() {
  }

  public static void init() {
    if (messagePool == null) {
      synchronized (messagePool.getClass()) {
        if (messagePool == null) {
          messagePool = new ConcurrentHashMap<String, PushMessageQueue>();
        }
      }
    }
  }

  public String getOneMessageByID(String id) {
    return messagePool.get(id).poll();
  }

  public void addOneMessage(String id, String message) {
    messagePool.get(id).put(message);
  }
}
