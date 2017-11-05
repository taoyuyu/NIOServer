package com.whu.yves.message;

import org.junit.Test;

/**
 * Created by yutao on 17/11/2.
 */
public class TestMessagePool {
  @Test
  public void testPool() {
    MessagePool.addOneMessage("taoyu", "this is a message1");
    MessagePool.addOneMessage("taoyu", "this is a message2");
    String message;
    while ((message = MessagePool.getOneMessageByID("taoyu"))!=null) {
      System.out.println(message);
    }
    System.out.println(MessagePool.getOneMessageByID("taoyu1"));
  }
}