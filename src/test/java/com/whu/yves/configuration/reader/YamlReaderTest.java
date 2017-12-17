package com.whu.yves.configuration.reader;

import org.apache.log4j.Logger;
import org.junit.Test;

public class YamlReaderTest {
  private static Logger LOG = Logger.getLogger(YamlReader.class);
  @Test
  public void testYamlReader() {
    YamlReader.prepare("conf/app.yaml");
    LOG.info(YamlReader.getRandom());
  }
}
