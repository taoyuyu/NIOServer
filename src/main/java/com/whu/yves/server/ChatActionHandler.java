package com.whu.yves.server;

import com.whu.yves.message.MessagePackager;
import com.whu.yves.message.MessagePool;
import com.whu.yves.protocal.MessageType;
import com.whu.yves.protocal.Parser;
import com.whu.yves.server.task.MessageHandleTask;
import com.whu.yves.server.task.ThreadPoolService;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import org.apache.log4j.Logger;

/**
 * Created by yutao on 17/9/14.
 * 读写事件响应
 */
public class ChatActionHandler extends ActionHandler {

  private static Logger LOG = Logger.getLogger(ChatActionHandler.class);
  private int BYTE_BUFFER_SIZE = 1024;

  public ChatActionHandler(Selector selector) {
    super(selector);
  }

  @Override
  protected void readAction(SelectionKey key) {
    SocketChannel channel = (SocketChannel) key.channel();
    ByteBuffer buffer = ByteBuffer.allocate(BYTE_BUFFER_SIZE);
    try {
      int count = channel.read(buffer);
      if (count > 0) {
        ThreadPoolService.submit(new MessageHandleTask(channel, buffer));
      } else {
        channel.close();
      }
    } catch (IOException e) {

    }

  }



}
