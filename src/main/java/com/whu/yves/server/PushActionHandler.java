package com.whu.yves.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import org.apache.log4j.Logger;

/**
 * Created by yutao on 17/9/14.
 */
public class PushActionHandler extends ActionHandler {
  private static Logger LOG = Logger.getLogger(PushActionHandler.class);
  public PushActionHandler(Selector selector) {
    super(selector);
  }

  @Override
  protected void readAction(SelectionKey key) {
    SocketChannel channel = (SocketChannel) key.channel();
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    try {
      int count = channel.read(buffer);
      if (count > 0) {
        System.out.println("write socket id: " + channel.socket().hashCode());
        readByteBuffer(buffer);
        channel.write(ByteBuffer.wrap(("client id => " + key.hashCode() + "\n").getBytes()));
      } else {
        channel.close();
      }
    } catch (IOException ioe) {
      LOG.error(ioe.getStackTrace());
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
