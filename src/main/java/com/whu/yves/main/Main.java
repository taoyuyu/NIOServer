package com.whu.yves.main;

import com.whu.yves.server.ActionHandler;
import com.whu.yves.server.SelectorFactory;
import com.whu.yves.timing.ThreadPoolService;

/**
 * Created by yutao on 17/9/13.
 */
public class Main {

  public static void main(String[] args) {
    //创建推送线程
    ThreadPoolService.initPool(2);
    //开启NIOServer服务
    new ActionHandler(SelectorFactory.getSelector(7681)).listen();
  }
}
