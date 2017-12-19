package com.whu.yves.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import org.apache.log4j.Logger;

/**
 * Created by yutao on 17/9/11.
 */
public class SelectorFactory {
  private static Logger LOG = Logger.getLogger(SelectorFactory.class);
  private static Selector selector = null;
  private static int PORT;

  private SelectorFactory() {
  }

  public static Selector getSelector(int port) {
    if (selector == null) {
      synchronized (Selector.class) {
        if (selector == null) {
          try {
            PORT = port;
            selector = Selector.open();
            init();
          } catch (IOException ioe) {
            LOG.error(ioe.getMessage());
            return null;
          }
        }
      }
    }
    return selector;
  }

  private static void init() throws IOException {
    ServerSocketChannel server = ServerSocketChannel.open();
    //设置为非阻塞模式
    server.configureBlocking(false);
    ServerSocket socket = server.socket();
    InetSocketAddress address = new InetSocketAddress(PORT);
    //绑定端口
    socket.bind(address);
    //注册事件
    server.register(selector, SelectionKey.OP_ACCEPT);
    LOG.info(String.format("Open port %d succeed!!!", PORT));
  }
}
