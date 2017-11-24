package com.whu.yves.protocal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;
import org.jdom.input.SAXBuilder;

/**
 * Created by yutao on 17/9/14.
 */
public class Parser {

  private static Logger LOG = Logger.getLogger(Parser.class);
  private Document document = null;
  private String message;

  public Parser(String message) {
    this.message = message;
    StringReader reader = new StringReader(message);
    InputSource source = new InputSource(reader);
    SAXBuilder builder = new SAXBuilder();
    try {
      document = builder.build(source);
      addTimeStamp();
    } catch (JDOMException | IOException e) {
      LOG.error("Parser xml message error: " + message);
      throw new RuntimeException("Parser xml message error: " + message);
    }
  }

  private void addTimeStamp() {
    Element element = new Element("time");
    element.addContent(String.valueOf(System.currentTimeMillis()));
    document.getRootElement().addContent(element);
  }

  public MessageType getMessageType() {
    if (document == null) {
      LOG.error("Read message error: " + message);
      return null;
    }
    Element root = document.getRootElement();
    String type = root.getChild("body").getAttributeValue("type");
    return MessageType.valueOf(type.toUpperCase());
  }

  public String getMessageContent() {
    if (document == null) {
      LOG.error("Read message error: " + message);
      return null;
    }
    Element root = document.getRootElement();
    Element body = root.getChild("body");
    if (null == body) {
      LOG.error("No body in xml message: " + message);
      return null;
    }
    return body.getChildText("content");
  }

  public String getMessageTarget() {
    if (document == null) {
      LOG.error("Read message error: " + message);
      return null;
    }
    Element root = document.getRootElement();
    Element body = root.getChild("body");
    if (null == body) {
      LOG.error("No body in xml message: " + message);
    }
    return body.getChildText("target");
  }

  public String getID() {
    if (document == null) {
      LOG.error("Read message error: " + message);
    }
    Element root = document.getRootElement();
    String id = root.getChild("body").getAttributeValue("id");
    return id;
  }

  public Document getDocument() {
    return document;
  }

  @Override
  public String toString() {
    Format format = Format.getPrettyFormat();
    format.setEncoding("UTF-8");// 设置xml文件的字符为UTF-8，解决中文问题
    XMLOutputter xmlout = new XMLOutputter(format);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      xmlout.output(document, baos);
      return baos.toString();
    } catch (IOException ioe) {
      return null;
    }
  }

}
