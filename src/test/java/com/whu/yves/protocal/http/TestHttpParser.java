package com.whu.yves.protocal.http;

import com.whu.yves.protocal.Parser;
import org.apache.log4j.Logger;
import org.junit.Test;

public class TestHttpParser {
  private static Logger LOG = Logger.getLogger(TestHttpParser.class);

  @Test
  public void testParser() {
    String header = "GET / HTTP/1.1\n"
        + "Host: localhost:7682\n"
        + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n"
        + "Connection: keep-alive\n"
        + "Cookie: Pycharm-cbaa752b=53853178-5118-41da-8327-b9e9393e86dc; Pycharm-cbaa752c=ae054395-b5b3-4899-a4ff-348cc5ec4024; _ga=GA1.1.2086357097.1507967480; bdshare_firstime=1509540484784\n"
        + "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/601.7.7 (KHTML, like Gecko) Version/9.1.2 Safari/601.7.7\n"
        + "Accept-Language: zh-cn\n"
        + "Cache-Control: max-age=0\n"
        + "Accept-Encoding: gzip, deflate";

    RequestParser parser = new RequestParser(header);
    parser.parse();
    LOG.info(parser.getRequest());
    LOG.info(parser.getUri());
    LOG.info(parser.getMethod());
  }
}
