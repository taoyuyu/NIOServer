package com.whu.yves.protocal;

import java.io.IOException;
import java.io.StringReader;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.xml.sax.InputSource;
import org.jdom.input.SAXBuilder;

/**
 * Created by yutao on 17/9/14.
 */
public class Parser {

  private static Logger LOG = Logger.getLogger(Parser.class);
  private Document document = null;

  public Parser(String message) {
    StringReader reader = new StringReader(message);
    InputSource source = new InputSource(reader);
    SAXBuilder builder = new SAXBuilder();
    try {
      document = builder.build(source);
    } catch (JDOMException | IOException e) {
      LOG.error(e.getStackTrace());
    }
  }

  public MessageType getMessageType() {
    if (document == null) {
      LOG.error("No type in xml message: " + document.toString());
      throw new RuntimeException("read xml error");
    }
    Element root = document.getRootElement();
    String type = root.getChild("body").getAttributeValue("type");
    return MessageType.valueOf(type.toUpperCase());
  }

  public String getMessageContent() {
    if (document == null) {
      throw new RuntimeException("read xml error");
    }
    Element root = document.getRootElement();
    Element body = root.getChild("body");
    if (null == body) {
      LOG.error("No body in xml message: " + document.toString());
      throw new RuntimeException("No body in xml message");
    }
    return body.getChildText("content");
  }

  public String getMessageTarget() {
    if (document == null) {
      throw new RuntimeException("read xml error");
    }
    Element root = document.getRootElement();
    Element body = root.getChild("body");
    if (null == body) {
      LOG.error("No body in xml message: " + document.toString());
      throw new RuntimeException("No body in xml message");
    }
    return body.getChildText("target");
  }

  public String getID() {
    if (document == null) {
      throw new RuntimeException("read xml error");
    }
    Element root = document.getRootElement();
    String id = root.getChild("body").getAttributeValue("id");
    return id;
  }

}
