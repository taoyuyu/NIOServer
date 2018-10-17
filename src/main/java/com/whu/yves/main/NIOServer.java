package com.whu.yves.main;

import com.whu.yves.configuration.reader.YamlReader;
import com.whu.yves.protocal.http.HttpProxy;
import com.whu.yves.server.ChatActionHandler;
import com.whu.yves.server.SelectorFactory;
import com.whu.yves.server.task.ThreadPoolService;

import org.apache.log4j.Logger;

/**
 * Created by yutao on 17/9/13.
 */
public class NIOServer {
  private static Logger LOG = Logger.getLogger(NIOServer.class);
  public static void main(String[] args) {
    YamlReader.prepare(args[0]);
    //创建服务线程
    ThreadPoolService.init(YamlReader.getNThread());
    //关闭线程池
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("shutdonw succeed!");
      ThreadPoolService.shutdown();
    }));
    LOG.info("Thread Pool size: " + YamlReader.getNThread());
    //初始化代理
    HttpProxy.init();
    //开启NIOServer服务
    new ChatActionHandler(SelectorFactory.getSelector(YamlReader.getPort())).listen();

  }
}
