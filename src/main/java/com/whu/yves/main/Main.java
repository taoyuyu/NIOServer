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
  public static void main(String[] args) {
    int nThread = 2;
    //创建推送线程
    ThreadPoolService.initPool(nThread);
    LOG.info("Thread Pool size: " + nThread);
    //初始化消息池
    MessagePool.init();
    LOG.info("Start message pool succeed");
    //开启NIOServer服务
    new ChatActionHandler(SelectorFactory.getSelector(7682)).listen();
  }
}
