package com.whu.yves.protocal;

public abstract class Parser implements AbstractParser {
  protected String message;
  public Parser(String message) {
    this.message = message;
  }
}
