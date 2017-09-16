package com.whu.yves.configuration;

import com.whu.yves.protocal.Parser;
import org.junit.Test;

/**
 * Created by yutao on 17/9/13.
 */
public class XmlReader {
  @Test
  public void testMessage() {
    String message = "<messages>\n"
        + "  <body type=\"heart-beat\">\n"
        + "    <content>this is a message</content>\n"
        + "  </body>\n"
        + "</messages>";
    Parser parser = new Parser(message);
    System.out.println(parser.getMessageType());
    System.out.println(parser.getMessageContent());
  }
}
