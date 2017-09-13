package com.whu.yves.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by yutao on 17/9/11.
 */
public class ActionHandler {

  private Selector selector = null;

  public ActionHandler(Selector selector) {
    this.selector = selector;
  }

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
      ioe.printStackTrace();
    }
  }

  private void process(SelectionKey key) throws IOException {
    if (key.isAcceptable()) {
      // 接收请求
      ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
      SocketChannel channel = ssc.accept();
      channel.configureBlocking(false);
      channel.register(selector, SelectionKey.OP_READ);
      System.out.println("connection id: " + channel.socket().hashCode());
    } else if (key.isReadable()) {
      // 读事件
      SocketChannel channel = (SocketChannel) key.channel();
      ByteBuffer buffer = ByteBuffer.allocate(1024);
      int count = channel.read(buffer);
      if (count > 0) {
        System.out.println("write socket id: " + channel.socket().hashCode());
        readByteBuffer(buffer);
        channel.write(ByteBuffer.wrap(("client id => " + key.hashCode() + "\n").getBytes()));
      } else {
        channel.close();
      }
    } else if (key.isWritable()) {
      // 写事件
      System.out.println("write channel");
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

  private void readByteBuffer(ByteBuffer buffer) {
    buffer.flip();
    while (buffer.hasRemaining()) {
      System.out.print((char) buffer.get());
    }
    buffer.clear();
  }

}
