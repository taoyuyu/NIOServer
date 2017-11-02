package com.whu.yves.message;

/**
 * Created by yutao on 17/11/2.
 */
public class MessagePackager {
  private MessagePackager(){}
  public static String responseHeartBeat(String id) {
    return String.format("<messages>\n"
        + "  <body type=\"response_heart_beat\" id=\"%s\">\n"
        + "  </body>\n"
        + "</messages>", id);
  }
}
