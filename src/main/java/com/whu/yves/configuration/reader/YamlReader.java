package com.whu.yves.configuration.reader;

import com.whu.yves.protocal.UtilStrings;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.ho.yaml.Yaml;

public class YamlReader {
  private static Logger LOG = Logger.getLogger(YamlReader.class);
  private HashMap configuration = null;
  public YamlReader(String path) {
    File file = new File(path);
    try {
      configuration = Yaml.loadType(new FileInputStream(file), HashMap.class);
    } catch (FileNotFoundException fne) {
      LOG.error(String.format("file %s not found", file.getAbsoluteFile()));
      throw new RuntimeException();
    }
  }

  public Integer getPort() {
    return (Integer) configuration.get(UtilStrings.PORT);
  }

  public Integer getNThread() {
    return (Integer) configuration.get(UtilStrings.N_THREAD);
  }

  @Override
  public String toString() {
    return configuration.toString();
  }
}

