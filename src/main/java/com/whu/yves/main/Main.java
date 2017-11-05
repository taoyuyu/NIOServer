package com.whu.yves.main;

import com.whu.yves.message.MessagePool;
import com.whu.yves.server.ChatActionHandler;
import com.whu.yves.server.SelectorFactory;
import com.whu.yves.timing.ThreadPoolService;

import org.apache.log4j.Logger;

/**
 * Created by yutao on 17/9/13.
 */
public class Main {
  private static Logger LOG = Logger.getLogger(Main.class);
  private static int N_THREAD = 2;
  private static int PORT = 7682;
  public static void main(String[] args) {
    //创建推送线程
    ThreadPoolService.initPool(N_THREAD);
    LOG.info("Thread Pool size: " + N_THREAD);
    //开启NIOServer服务
    new ChatActionHandler(SelectorFactory.getSelector(PORT)).listen();
  }
}
