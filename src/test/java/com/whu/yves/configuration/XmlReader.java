package com.whu.yves.configuration;

import com.whu.yves.protocal.xml.XMLParser;
import org.junit.Test;

/**
 * Created by yutao on 17/9/13.
 */
public class XmlReader {
  @Test
  public void testMessage() {
    String message = "<messages>\n"
        + "  <body type=\"heart_beat\">\n"
        + "    <target>taoyu</target>\n"
        + "    <content>this is a message</content>\n"
        + "  </body>\n"
        + "</messages>";
    XMLParser parser = new XMLParser(message);
    System.out.println(parser.getMessageType());
    System.out.println(parser.getMessageTarget());
    System.out.println(parser.getMessageContent());
  }
}
