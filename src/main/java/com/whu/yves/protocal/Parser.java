package com.whu.yves.protocal;

public class Parser implements AbstractParser {
  protected String message;
  public Parser(String message) {
    this.message = message;
  }

  @Override
  public boolean check() {
    return false;
  }

  @Override
  public void parse() {
  }
}
