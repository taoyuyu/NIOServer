package com.whu.yves.protocal;

import java.io.IOException;
import java.io.StringReader;
import org.
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

  private Logger LOG = Logger.getLogger(Parser.class);
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
      throw new RuntimeException("read xml error");
    }
    Element root = document.getRootElement();


  }
}
