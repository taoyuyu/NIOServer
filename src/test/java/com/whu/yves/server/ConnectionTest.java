package com.whu.yves.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionTest {
  //netstat -antp | grep 13301 | wc -l 统计服务端13301端口socket链接数

  private static Vector<Socket> sockets = new Vector<>();
  private static ExecutorService service = Executors.newFixedThreadPool(4);
  public static void main(String args[]) {
    final CountDownLatch latch = new CountDownLatch(1024);
    try {
      Runnable task = new Runnable() {
        @Override
        public void run() {
          try {
            Socket socket = new Socket("123.207.4.30", 7682);
            sockets.add(socket);
            latch.countDown();
          } catch (IOException ioe) {
            ioe.printStackTrace();
          }
        }
      };
      for (int i=0; i<1024; ++i) {
        service.submit(task);
      }
      latch.await();
      System.out.println("Connection established!!!");
      BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
      int index;
      while ((index = Integer.valueOf(sin.readLine())) != -1) {
        writeMessage(sockets.get(index), sin);
      }
      //关闭socket连接
      closeSockets();
    } catch (Exception e) {
      System.out.println("ERROR: " + e);
    }
    //关闭线程池
    service.shutdown();
  }

  private static void writeMessage(Socket socket, BufferedReader sin) throws Exception {
    System.out.println("write socket => " + socket.getLocalSocketAddress());
    //由系统标准输入设备构造BufferedReader对象
    PrintWriter os = new PrintWriter(socket.getOutputStream());
    //由Socket对象得到输出流，并构造PrintWriter对象
    BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    //由Socket对象得到输入流，并构造相应的BufferedReader对象
    String readline;
    readline = sin.readLine(); //从系统标准输入读入一字符串
    while (!readline.equals("bye")) {
      //若从标准输入读入的字符串为 "bye"则停止循环
      os.println(readline);
      //将从系统标准输入读入的字符串输出到Server
      os.flush();
      //在系统标准输出上打印读入的字符串
      System.out.println("Server:" + is.readLine());
      //从Server读入一字符串，并打印到标准输出上
      readline = sin.readLine(); //从系统标准输入读入一字符串
    }
    System.out.println("leave succeed!!!");
  }

  private static void closeSockets() throws IOException {
    for(Socket socket : sockets) {
      socket.close();
    }
  }
}
