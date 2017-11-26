package com.whu.yves.server.task;

import com.whu.yves.message.MessagePackager;
import com.whu.yves.message.MessagePool;
import com.whu.yves.protocal.http.HttpParser;
import com.whu.yves.protocal.xml.MessageType;
import com.whu.yves.protocal.xml.XMLParser;
import com.whu.yves.server.Connections;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.apache.log4j.Logger;

/**
 * Created by yutao on 17/11/22.
 */
public class MessageHandleTask extends HandleTask {

  private static Logger LOG = Logger.getLogger(MessageHandleTask.class);

  public MessageHandleTask(SocketChannel channel, ByteBuffer buffer) {
    super(channel, buffer);
  }

  @Override
  public void run() {
    try {
      LOG.info("write socket id: " + channel.socket().hashCode());
      String message = readByteBuffer(buffer);
      XMLParser xmlParser = new XMLParser(message);
      if (xmlParser.check()) {
        xmlParser.parse();
        MessageType type = xmlParser.getMessageType();
        switch (type) {
          case IDENTIFY:
            // 获取用户id, 根据id在MessagePool中读取历史消息, 回写消息
            identify(xmlParser, channel);
            break;
          case HEART_BEAT:
            // 回复收到消息
            heartBeat(xmlParser, channel);
            break;
          case SHORT_MESSAGE:
            // 查询消息目标用户, 转发消息. 用户离线/在线
            shortMessage(xmlParser, channel);
            break;
          case CLOSE_CONNECTION:
            closeConnection(xmlParser, channel);
            break;
          default:
            LOG.error("Undefined message type");
        }
      } else {
        HttpParser httpParser = new HttpParser(message);
        if (httpParser.check()) {
          httpParser.parse();
          String uri = httpParser.getUri();
          if (uri.equals("/")) {
            channel.write(ByteBuffer.wrap(("HTTP/1.1 200 File Not Found\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: 23\r\n" +
                "\r\n" +
                "<h1>Welcome to NIOServer</h1>").getBytes()));
          } else {
            channel.write(ByteBuffer.wrap(("HTTP/1.1 404 File Not Found\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: 23\r\n" +
                "\r\n" +
                "<h1>ERROR:File Not Found</h1>").getBytes()));
          }
          channel.close();
        }
      }
    } catch (IOException e) {
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

  private void identify(XMLParser parser, SocketChannel channel) throws IOException {
    String id = parser.getID();
    Connections.putOneConnection(id, channel);
    String message;
    while ((message = MessagePool.getOneMessageByID(id)) != null) {
      channel.write(ByteBuffer.wrap(message.getBytes()));
    }
  }

  private void heartBeat(XMLParser parser, SocketChannel channel) throws IOException {
    String id = parser.getID();
    channel
        .write(ByteBuffer.wrap(MessagePackager.responseHeartBeat(parser.getDocument()).getBytes()));
  }

  private void shortMessage(XMLParser parser, SocketChannel channel) throws IOException {
    if (!Connections.checkConnection(parser.getID(), channel)) {
      return;
    }
    String target = parser.getMessageTarget();
    if (!Connections.sendOneMessage(target, parser.toString())) {
      // failed
      MessagePool.addOneMessage(target, parser.toString());
      channel.write(ByteBuffer
          .wrap(MessagePackager.responseReceiveMessage(parser.getDocument(), false).getBytes()));
    } else {
      // succeed
      channel.write(ByteBuffer
          .wrap(MessagePackager.responseReceiveMessage(parser.getDocument(), true).getBytes()));
    }
  }

  private void closeConnection(XMLParser parser, SocketChannel channel) {
    String id = parser.getID();
    if (!Connections.checkConnection(id, channel)) {
      return;
    }
    Connections.closeOneConnection(id, channel);
  }
}
