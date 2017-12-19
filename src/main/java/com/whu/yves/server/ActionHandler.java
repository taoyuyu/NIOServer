package com.whu.yves.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;

public class ActionHandler implements DefaultActionHandler {

  private static Logger LOG = Logger.getLogger(ActionHandler.class);
  private Selector selector;

  public ActionHandler(Selector selector) {
    this.selector = selector;
  }

  @Override
  public void listen() {
    if(selector == null) {
      return;
    }
    try {
      while (true) {
        //调用会阻塞，直到有一个事件发生
        selector.select();
        //获取事件的selectionKey
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          iterator.remove();
          process(key);
        }
      }
    } catch (IOException ioe) {
      LOG.error(ioe.getMessage());
    }
  }

  private void process(SelectionKey key) {
    if (key.isAcceptable()) {
      // 接收请求
      acceptAction(key);
    } else if (key.isReadable()) {
      // 读事件
      readAction(key);
    } else if (key.isWritable()) {
      // 写事件
      LOG.info("write channel");
      writeAction(key);
    }
  }

  protected void acceptAction(SelectionKey key) {
    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
    try {
      SocketChannel channel = ssc.accept();
      LOG.info(String.format("client %d connect established", channel.socket().hashCode()));
      channel.configureBlocking(false);
      channel.register(selector, SelectionKey.OP_READ);
    } catch (IOException ioe) {
      LOG.info(ioe.getStackTrace());
    }
  }

  protected void readAction(SelectionKey key) {
  }

  protected void writeAction(SelectionKey key) {
  }

}
