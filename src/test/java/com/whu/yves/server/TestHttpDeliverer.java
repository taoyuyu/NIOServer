package com.whu.yves.server;

import com.sun.deploy.net.HttpResponse;
import java.io.IOException;
import java.util.Set;
import javax.xml.ws.spi.http.HttpContext;
import org.junit.Test;

public class TestHttpDeliverer {
  @Test
  public void testDeliverer() throws IOException {
    String request = "GET /a HTTP/1.1\n"
        + "Host: 127.0.0.1:8080\n"
        + "Connection: keep-alive\n"
        + "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.59 Safari/537.36\n"
        + "Upgrade-Insecure-Requests: 1\n"
        + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n"
        + "Accept-Encoding: gzip, deflate, br\n"
        + "Accept-Language: zh-CN,zh;q=0.9,ja;q=0.8,en;q=0.7\n"
        + "Cookie: Pycharm-cbaa752b=53853178-5118-41da-8327-b9e9393e86dc; Pycharm-cbaa752c=ae054395-b5b3-4899-a4ff-348cc5ec4024";
    HttpDeliverer deliverer = new HttpDeliverer(request);
    deliverer.deliver("127.0.0.1:8080");
  }
}

