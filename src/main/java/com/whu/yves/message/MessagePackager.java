package com.whu.yves.message;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

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

  public static String responseReceiveMessage(Document document, boolean isReceived) {
    Element body = document.getRootElement().getChild("body");
    body.removeChild("content");
    Element element = new Element("result");
    element.addContent(String.format("%b", isReceived));
    body.addContent(element);

    Format format = Format.getPrettyFormat();
    format.setEncoding("UTF-8");
    XMLOutputter xmlout = new XMLOutputter(format);
    ByteArrayOutputStream bo = new ByteArrayOutputStream();
    try {
      xmlout.output(document, bo);
      return bo.toString();
    } catch (IOException ioe) {
      System.out.println("error");
      return null;
    }
  }
}
