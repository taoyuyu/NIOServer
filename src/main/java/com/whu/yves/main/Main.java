package com.whu.yves.main;

import com.whu.yves.server.PushActionHandler;
import com.whu.yves.server.SelectorFactory;
import com.whu.yves.timing.ThreadPoolService;

import org.apache.log4j.Logger;

/**
 * Created by yutao on 17/9/13.
 */
public class Main {
  private static Logger LOG = Logger.getLogger(Main.class);
  public static void main(String[] args) {
    //创建推送线程
    ThreadPoolService.initPool(2);
    LOG.info("Start push thread succeed!");
    //开启NIOServer服务
    new PushActionHandler(SelectorFactory.getSelector(7682)).listen();
  }
}
