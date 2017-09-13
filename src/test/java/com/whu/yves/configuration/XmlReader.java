package com.whu.yves.configuration;

import java.io.File;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.Test;

/**
 * Created by yutao on 17/9/13.
 */
public class XmlReader {
  @Test
  public void testMessage() throws ConfigurationException {
    File file = new File("conf/message.xml");
    System.out.println(file.getAbsolutePath());
    XMLConfiguration conf = new XMLConfiguration(file);
    System.out.println(conf.getString("body[@type]"));
    System.out.println(conf.getString("body.message"));
  }

}
