package com.whu.yves.timing;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ThreadPoolService {

  private static ScheduledExecutorService threadPool = null;
  private static Map<Integer, ScheduledFuture<?>> results = new HashMap<Integer, ScheduledFuture<?>>();
  private ThreadPoolService() {
  }

  public static void initPool(int coreSize) {
    if (threadPool == null) {
      synchronized (ScheduledExecutorService.class) {
        if (threadPool == null) {
          threadPool = Executors.newScheduledThreadPool(coreSize);
        }
      }
    } else {
      throw new RuntimeException("thread pool already init");
    }
  }

  public static int submit(ScheduleTask task, int start, int delay) {
    ScheduledFuture<?> token = threadPool.scheduleAtFixedRate(task, start, delay, TimeUnit.SECONDS);
    results.put(token.hashCode(), token);
    return token.hashCode();
  }

  public static int submit(Runnable runnable, int start, int delay) {
    ScheduledFuture<?> token = threadPool.scheduleAtFixedRate(runnable, start, delay, TimeUnit.SECONDS);
    results.put(token.hashCode(), token);
    return token.hashCode();
  }

  public static void cancel(int hashCode) {
    ScheduledFuture<?> result = results.get(hashCode);
    if (result == null) {
      throw new RuntimeException("no such task");
    } else {
      result.cancel(false);
      results.remove(hashCode);
    }
  }

  public static void shutdown() {
    threadPool.shutdown();
  }
}
