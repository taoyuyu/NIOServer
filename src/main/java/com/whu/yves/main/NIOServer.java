package com.whu.yves.main;

import com.whu.yves.configuration.reader.YamlReader;
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
    YamlReader reader = new YamlReader("conf/app.yaml");
    System.out.println(reader.toString());
    //创建服务线程
    ThreadPoolService.init(reader.getNThread());
    LOG.info("Thread Pool size: " + reader.getNThread());
    //开启NIOServer服务
    new ChatActionHandler(SelectorFactory.getSelector(reader.getPort())).listen();
  }
}
