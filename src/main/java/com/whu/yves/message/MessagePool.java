package com.whu.yves.message;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yutao on 17/9/14.
 */
public class MessagePool {

  private static ConcurrentHashMap<String, MessageQueue> messagePool = new ConcurrentHashMap<>();

  private MessagePool() {
  }

  public static String getOneMessageByID(String id) {
    MessageQueue queue = messagePool.get(id);
    if (queue != null) {
      return queue.poll();
    }
    return null;
  }

  public static void addOneMessage(String id, String message) {
    if(messagePool.containsKey(id)) {
      messagePool.get(id).put(message);
    } else {
      MessageQueue queue = new PushMessageQueue();
      queue.put(message);
      messagePool.put(id, queue);
    }

  }
}
