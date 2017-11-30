package com.whu.yves.protocal.xml;

import com.whu.yves.protocal.xml.XMLParser;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Created by yutao on 17/9/13.
 */
public class XmlReader {
  private static Logger LOG = Logger.getLogger(XMLParser.class);
  @Test
  public void testMessage() {
    String message = "<messages>\n"
        + "  <body type=\"heart_beat\">\n"
        + "    <target>taoyu</target>\n"
        + "    <content>this is a message</content>\n"
        + "  </body>\n"
        + "</messages>";
    XMLParser parser = new XMLParser(message);
    LOG.info(parser.getMessageType());
    LOG.info(parser.getMessageTarget());
    LOG.info(parser.getMessageContent());
  }
}
