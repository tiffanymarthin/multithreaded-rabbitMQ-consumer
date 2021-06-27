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
  private static Integer test_insert = 0;

  public static void main(String[] args) {
    if (args.length == 2) {
      TABLE_NAME = args[0];
      test_insert = Integer.parseInt(args[1]);
    }

    Properties prop = new Properties();
    try (InputStream input = Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("config.properties")) {
      prop.load(input);
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    WordCountDao wordCountDao = new WordCountDao();
    try {
      wordCountDao.createTable(TABLE_NAME);
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
//    logger.info("Table is created");
    if(test_insert == 1) {
      logger.info("Attempt to insert some items to table...");
      String message = "('word1', 100), ('word2', 300);";
      try {
        wordCountDao.createWordCount(TABLE_NAME, message);
        logger.info("successful insert!");
      } catch (Exception e) {
        logger.info(e.getMessage());
      }
    }
  }
}
