package com.whu.yves.protocal.xml;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Created by yutao on 17/11/2.
 */
public class XMLPackager {
  private XMLPackager(){}
  public static String responseHeartBeat(Document document) {
    return documentToString(document);
  }

  public static String responseReceiveMessage(Document document, boolean isReceived) {
    Element body = document.getRootElement().getChild("body");
    body.setAttribute("type", "response");
    body.removeChild("content");
    Element element = new Element("result");
    element.addContent(String.format("%b", isReceived));
    body.addContent(element);

    return documentToString(document);
  }

  private static String documentToString(Document document) {
    Format format = Format.getPrettyFormat();
    format.setEncoding("UTF-8");
    XMLOutputter xmlout = new XMLOutputter(format);
    ByteArrayOutputStream bo = new ByteArrayOutputStream();
    try {
      xmlout.output(document, bo);
      return bo.toString();
    } catch (IOException ioe) {
      return null;
    }
  }
}
