package com.whu.yves.server;

import com.whu.yves.message.MessagePackager;
import com.whu.yves.message.MessagePool;
import com.whu.yves.protocal.MessageType;
import com.whu.yves.protocal.Parser;
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
        LOG.info("write socket id: " + channel.socket().hashCode());
        String message = readByteBuffer(buffer);
        Parser parser = new Parser(message);
        MessageType type = parser.getMessageType();
        switch (type) {
          case IDENTIFY:
            // 获取用户id, 根据id在MessagePool中读取历史消息, 回写消息
            identify(parser, channel);
            break;
          case HEART_BEAT:
            // 回复收到消息
            heartBeat(parser, channel);
            break;
          case SHORT_MESSAGE:
            // 查询消息目标用户, 转发消息. 用户离线/在线
            /**
             * 目标用户在线则直接转发
             * 目标用户离线则写入MessagePool
             * */
            shortMessage(parser, channel);
            break;
          case CLOSE_CONNECTION:
            closeConnection(parser, channel);
            break;
          default:
            LOG.error("Undefined message type");
        }
      } else {
        channel.close();
      }
    } catch (IOException | RuntimeException e) {
      LOG.error(e.getMessage());
    }
  }

  private String readByteBuffer(ByteBuffer buffer) {
    StringBuilder sb = new StringBuilder();
    buffer.flip();
    while (buffer.hasRemaining()) {
      sb.append((char) buffer.get());
    }
    buffer.clear();
    LOG.info(String.format("message: %s", sb.toString()));
    return sb.toString();
  }

  private void identify(Parser parser, SocketChannel channel) throws IOException {
    String id = parser.getID();
    Connections.putOneConnection(id, channel);
    String message;
    while ((message = MessagePool.getOneMessageByID(id)) != null) {
      channel.write(ByteBuffer.wrap(message.getBytes()));
    }
  }

  private void heartBeat(Parser parser, SocketChannel channel) throws IOException {
    String id = parser.getID();
    channel.write(ByteBuffer.wrap(MessagePackager.responseHeartBeat(id).getBytes()));
  }

  private void shortMessage(Parser parser, SocketChannel channel) throws IOException {
    if (! Connections.checkConnection(parser.getID(), channel)) {
      return;
    }
    String target = parser.getMessageTarget();
    if (!Connections.sendOneMessage(target, parser.getMessage())) {
      MessagePool.addOneMessage(target, parser.getMessage());
    }
  }

  private void closeConnection(Parser parser, SocketChannel channel) {
    String id = parser.getID();
    if (! Connections.checkConnection(id, channel)) {
      return;
    }
    Connections.closeOneConnection(id, channel);
  }

}
