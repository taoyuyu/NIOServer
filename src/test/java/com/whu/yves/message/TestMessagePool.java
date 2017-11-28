package com.whu.yves.message;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Created by yutao on 17/11/2.
 */
public class TestMessagePool {
  private static Logger LOG = Logger.getLogger(TestMessagePool.class);

  @Test
  public void testPool() {
    MessagePool.addOneMessage("taoyu", "this is a message1");
    MessagePool.addOneMessage("taoyu", "this is a message2");
    String message;
    while ((message = MessagePool.getOneMessageByID("taoyu"))!=null) {
      LOG.info(message);
    }
    LOG.info(MessagePool.getOneMessageByID("taoyu1"));
  }
}