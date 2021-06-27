package cs6650.neu.a3.mysql;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.dbcp2.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBCPDataSource {
  private static BasicDataSource dataSource;
  private static final Logger logger = LogManager.getLogger(Main.class.getName());

  static {
    Properties prop = new Properties();
    try (InputStream input = Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("config.properties")) {
      prop.load(input);
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    final String HOST_NAME = prop.getProperty("mysql.ipaddress");
    final String PORT = prop.getProperty("mysql.port");
    final String DATABASE = "wordcountdb";
    final String USERNAME = prop.getProperty("mysql.username");
    final String PASSWORD = prop.getProperty("mysql.password");

   // https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html
    dataSource = new BasicDataSource();
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      logger.info("find class for name");
    } catch (ClassNotFoundException e) {
      logger.info(e.getMessage());
    }
    String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", HOST_NAME, PORT, DATABASE);
    dataSource.setUrl(url);
    dataSource.setUsername(USERNAME);
    dataSource.setPassword(PASSWORD);
    dataSource.setInitialSize(50);
    dataSource.setMaxTotal(300);
  }

  public static BasicDataSource getDataSource() {
    return dataSource;
  }
}
