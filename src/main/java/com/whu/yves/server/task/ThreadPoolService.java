package com.whu.yves.server.task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 * Created by yutao on 17/11/22.
 */
public class ThreadPoolService {
  private static ScheduledExecutorService scheduledExecutorService = null;
  private static Logger LOG = Logger.getLogger(ThreadPoolService.class);
  private ThreadPoolService() {
  }

  public static void init(int nThread) {
    if (scheduledExecutorService == null) {
      synchronized (ExecutorService.class) {
        if (scheduledExecutorService == null) {
          scheduledExecutorService = Executors.newScheduledThreadPool(nThread);
        }
      }
    } else {
      LOG.info("ThreadPool already initialized!!!");
    }
  }

  public static Future<?> submit(Runnable task, long start, long delay) {
    return scheduledExecutorService.scheduleWithFixedDelay(task, start, delay, TimeUnit.SECONDS);
  }

  public static Future<?> submit(Runnable task) {
    return scheduledExecutorService.submit(task);
  }
  public static Future<?> submit(Callable task) {
    return scheduledExecutorService.submit(task);
  }
  public static void shutdown() {
    scheduledExecutorService.shutdown();
  }

}
