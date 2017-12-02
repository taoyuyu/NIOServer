package com.whu.yves.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.whu.yves.db.dao.User;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;


/**
 * Created by yutao on 17/11/12.
 */
public class ConnectionUtils {
  Connection connection = null;
  public ConnectionUtils() {
    DataSource ds = new ComboPooledDataSource();
    try {
      this.connection = ds.getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  public static void main(String[] args) {
    ConnectionUtils connectionUtils = new ConnectionUtils();
    System.out.println(connectionUtils.getData());
  }

  public List<User> getData() {
    List<User> list = new ArrayList<>();
    if (connection == null) {
      System.out.println("connection is null");
      return null;
    }

    String sql = "SELECT name,age FROM user";
    PreparedStatement pStatement = null;
    ResultSet rSet = null;
    try {
      pStatement = connection.prepareStatement(sql);
      rSet = pStatement.executeQuery();
      while (rSet.next()) {
        User user = new User(rSet.getString("name"), rSet.getInt("age"));
        list.add(user);
      }
      return list;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    finally {
      try {
        if (rSet != null) {
          rSet.close();
        }
        if (pStatement != null) {
          pStatement.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return null;
  }


  private static Connection getConnection() {

    return null;

        /*String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/mysql";
        String user = "root";
        String password = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;*/

  }

}

