package com.whu.yves.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

/**
 * Created by yutao on 17/11/2.
 * 保存用户名与Channel的映射
 */
public class Connections {

  private static Logger LOG = Logger.getLogger(Connections.class);
  private static ConcurrentHashMap<String, SelectableChannel> connections = new ConcurrentHashMap<>();

  private Connections() {
  }

  public static void putOneConnection(String id, SelectableChannel channel) {
    SelectableChannel oldChannel = connections.get(id);
    if (oldChannel == null) {
      connections.put(id, channel);
    } else {
      try {
        if (oldChannel != channel) {
          oldChannel.close();
          connections.put(id, channel);
        }
      } catch (IOException e) {

      }
    }
  }

  public static boolean checkConnection(String id, SelectableChannel channel) {
    SelectableChannel checkedChannel = connections.get(id);
    if (checkedChannel == null) {
      LOG.info(String.format("user id: %s unchecked", id));
      return false;
    }
    if (checkedChannel != channel) {
      LOG.info(String.format("user id: %s check failed", id));
      return false;
    }
    return true;
  }

  public static boolean sendOneMessage(String id, String message) {
    SocketChannel channel = (SocketChannel) connections.get(id);
    if (null == channel) {
      LOG.info(String.format("user %s doesn't log in", id));
      return false;
    }
    try {
      channel.write(ByteBuffer.wrap((message).getBytes()));
    } catch (IOException ioe) {
      LOG.info(String.format("user %s is offline", id));
      try {
        channel.close();
      } catch (IOException ioe1) {
        LOG.error("close channel error: id = " + id);
      } finally {
        connections.remove(id);
      }

      return false;
    }
    return true;
  }

  public static void closeOneConnection(String id, SelectableChannel channel) {
    try {
      channel.close();
      connections.remove(id);
    } catch (IOException e) {

    }
  }
}
