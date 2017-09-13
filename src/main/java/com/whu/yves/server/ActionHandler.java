package com.whu.yves.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * Created by yutao on 17/9/11.
 */
public class ActionHandler implements DefaultActionHandler {

  private static Logger LOG = Logger.getLogger(ActionHandler.class);
  private Selector selector = null;

  public ActionHandler(Selector selector) {
    this.selector = selector;
  }

  @Override
  public void listen() {
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
      LOG.error(ioe.getStackTrace());
    }
  }

  private void process(SelectionKey key) throws IOException {
    if (key.isAcceptable()) {
      // 接收请求
      acceptAction(key);
    } else if (key.isReadable()) {
      // 读事件
      LOG.info("read channel");
      readAction(key);
    } else if (key.isWritable()) {
      // 写事件
      LOG.info("write channel");
      write(key);
    }
  }

  protected void acceptAction(SelectionKey key) throws IOException {
    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
    SocketChannel channel = ssc.accept();
    LOG.info(String.format("client %d connect established", channel.socket().hashCode()));
    channel.configureBlocking(false);
    channel.register(selector, SelectionKey.OP_READ);
  }

  protected void readAction(SelectionKey key) throws IOException {

  }

  protected void write(SelectionKey key) throws IOException {
    SocketChannel channel = (SocketChannel) key.channel();
    String name = (String) key.attachment();
    ByteBuffer block = ByteBuffer.wrap(("Hello" + name).getBytes());
    if (block != null) {
      channel.write(block);
    } else {
      channel.close();
    }
  }



}
