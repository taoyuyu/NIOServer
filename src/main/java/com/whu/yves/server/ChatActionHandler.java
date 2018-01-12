package com.whu.yves.server;

import com.whu.yves.server.task.MessageHandleTask;
import com.whu.yves.server.task.ThreadPoolService;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;

/**
 * Created by yutao on 17/9/14.
 * 读写事件响应
 */
public class ChatActionHandler extends ActionHandler {

  private static Logger LOG = Logger.getLogger(ChatActionHandler.class);
  private static ConcurrentHashMap<Integer, Future<?>> tasks = new ConcurrentHashMap<Integer, Future<?>>();
  private int BYTE_BUFFER_SIZE = 1024;

  public ChatActionHandler(Selector selector) {
    super(selector);
  }

  @Override
  protected void readAction(SelectionKey key) {
    SocketChannel channel = (SocketChannel) key.channel();
    LOG.info(channel.socket().hashCode() + " read channel");
    ByteBuffer buffer = ByteBuffer.allocate(BYTE_BUFFER_SIZE);
    try {
      int count = channel.read(buffer);
      if (count > 0) {
        Future<?> future = ThreadPoolService.submit(new MessageHandleTask(channel, buffer));
        tasks.put(channel.socket().hashCode(), future);
      } else {
        LOG.info(channel.socket().hashCode() + " closed");
        channel.close();
        Future<?> future = tasks.get(channel.socket().hashCode());
        if (future != null) {
          future.cancel(true);
          tasks.remove(channel.socket().hashCode());
          LOG.info("cancel a task");
        }
      }
    } catch (IOException e) {
      LOG.error("close channel error: " + e.getMessage());
    }
  }
}
