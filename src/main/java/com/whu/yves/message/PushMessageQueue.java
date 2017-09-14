package com.whu.yves.message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 * Created by yutao on 17/9/14.
 */
public class PushMessageQueue implements MessageQueue {

  private static Logger LOG = Logger.getLogger(PushMessageQueue.class);
  private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

  @Override
  public String poll(long timeOut) {
    String message = null;
    try {
      message = queue.poll(timeOut, TimeUnit.SECONDS);
    } catch (InterruptedException ie) {
      LOG.error("poll message from BlockingQueue error");
    }
    return message;
  }

  @Override
  public String poll() {
    return queue.poll();
  }

  @Override
  public void put(String message) {
    try {
      queue.put(message);
    } catch (InterruptedException ie) {
      LOG.error("put message to BlockingQueue error");
      throw new RuntimeException("put message to BlockingQueue error");
    }
  }

  @Override
  public void offer(String message) {
    queue.offer(message);
  }

  @Override
  public void offer(String message, long timeOut) {
    try {
      queue.offer(message, timeOut, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      LOG.error("offer message to BlockingQueue error");
      throw new RuntimeException("offer message to BlockingQueue error");
    }
  }
}
