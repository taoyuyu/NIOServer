package com.whu.yves.server.task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;

/**
 * Created by yutao on 17/11/22.
 */
public class ThreadPoolService {
  private static ExecutorService fixedExecutorServices = null;
  private static Logger LOG = Logger.getLogger(ThreadPoolService.class);
  private ThreadPoolService() {
  }

  public static void init(int nThread) {
    if (fixedExecutorServices == null) {
      synchronized (ExecutorService.class) {
        if (fixedExecutorServices == null) {
          fixedExecutorServices = Executors.newFixedThreadPool(nThread);
        }
      }
    } else {
      LOG.info("ThreadPool already initialized!!!");
    }
  }

  public static Future<?> submit(Runnable task) {
    return fixedExecutorServices.submit(task);
  }
  public static Future<?> submit(Callable task) {
    return fixedExecutorServices.submit(task);
  }
  public static void shutdown() {
    fixedExecutorServices.shutdown();
  }

}
