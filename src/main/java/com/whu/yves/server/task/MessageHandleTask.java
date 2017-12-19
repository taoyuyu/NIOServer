package com.whu.yves.server.task;

import com.whu.yves.protocal.http.HttpPackager;
import com.whu.yves.protocal.http.HttpProxy;
import com.whu.yves.protocal.xml.XMLPackager;
import com.whu.yves.message.MessagePool;
import com.whu.yves.protocal.http.RequestParser;
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
        return;
      }
      RequestParser requestParser = new RequestParser(message);
      if (requestParser.check()) {
        proxyModel(requestParser, channel);
      }
    } catch (IOException e) {
      LOG.error(e.getMessage());
    }
  }

//  private void proxyModel(RequestParser requestParser, SocketChannel channel) throws IOException {
//    requestParser.parse();
//    channel.write(ByteBuffer.wrap(new HttpPackager(requestParser).getResponse().getBytes()));
//    channel.close();
//  }

  private void proxyModel(RequestParser requestParser, SocketChannel channel) throws IOException {
    requestParser.parse();
    HttpProxy.proxy(requestParser, channel);
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
        .write(ByteBuffer.wrap(XMLPackager.responseHeartBeat(parser.getDocument()).getBytes()));
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
          .wrap(XMLPackager.responseReceiveMessage(parser.getDocument(), false).getBytes()));
    } else {
      // succeed
      channel.write(ByteBuffer
          .wrap(XMLPackager.responseReceiveMessage(parser.getDocument(), true).getBytes()));
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
