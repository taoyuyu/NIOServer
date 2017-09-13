package com.whu.yves.timing;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ScheduleTask implements Runnable {

  private BlockingQueue<String> resultQueue;
  private BlockingQueue<String> worksQueue;

  public ScheduleTask(BlockingQueue<String> worksQueue) {
    this.worksQueue = worksQueue;
    this.resultQueue = new LinkedBlockingQueue<String>();
  }

  public void run() {
    String url = null;
    try {
      url = worksQueue.poll(3, TimeUnit.SECONDS);

    } catch (Exception e) {
      throw new RuntimeException(String.format("extract %s error", url));
    }
  }

  public BlockingQueue<String> getResultQueue() {
    return this.resultQueue;
  }
}
