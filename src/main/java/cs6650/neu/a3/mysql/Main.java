package cs6650.neu.a3.mysql;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
  private static final Logger logger = LogManager.getLogger(Main.class.getName());
  private static Integer MAX_THREADS = 128;
  private static String TABLE_NAME = "wc1";

  public static void main(String[] args) {
    if (args.length == 1) {
      TABLE_NAME = args[0];
    }

    Properties prop = new Properties();
    try (InputStream input = Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("config.properties")) {
      prop.load(input);
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    WordCountDao wordCountDao = new WordCountDao();
    wordCountDao.createTable(TABLE_NAME);
//    logger.info("Table is created");
  }
}
