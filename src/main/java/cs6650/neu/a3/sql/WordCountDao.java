package cs6650.neu.a3.sql;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class WordCountDao {

  private static final Logger logger = LogManager.getLogger(WordCountDao.class.getName());
  private static HikariDataSource dataSource = DataSource.getDataSource();

  public WordCountDao() {
  }

  public void createTable(String tableName) {
//    Connection conn = null;
    logger.info("Getting remote connection from config.properties file...");
    try (Connection conn = dataSource.getConnection();
        Statement stmt = conn.createStatement();) {
      logger.info("Remote connection successful.");

      logger.info("Attempting to create table: " + tableName);
//      String createQueryStatement = "CREATE TABLE " + tableName +
//          "(id INT NOT NULL AUTO_INCREMENT, " +
//          " wordKey varchar(255), " +
//          " wordCount int, " +
//          " primary key (id));";
      String createQueryStatement = "CREATE TABLE " + tableName +
          "(wordKey varchar(255), " +
          " wordCount int);";
      stmt.executeUpdate(createQueryStatement);
      logger.info("Created table in given database... : " + tableName);
    } catch (SQLException e) {
      logger.info(e.getMessage());
    }
  }

  public void createWordCount(String tableName, String message) {
    String insertQueryStatement =
        "INSERT INTO " + tableName + "(wordKey, wordCount) values " + message;
//        + "ON DUPLICATE KEY UPDATE wordCount = VALUES(wordCount)";
    try (Connection conn = DataSource.getConnection();
        Statement stmt = conn.createStatement();
    ) {
      stmt.executeUpdate(insertQueryStatement);
    } catch (SQLException e) {
      logger.info(e.getMessage());
    }
  }

  public void deleteTable(String tableName) {
    String deleteQuery = "DROP TABLE " + tableName;
    try (Connection conn = DataSource.getConnection();
        Statement stmt = conn.createStatement();
    ) {
      stmt.executeUpdate(deleteQuery);
      logger.info("Table deleted in given database...");
    } catch (SQLException e) {
      logger.info(e.getMessage());
    }
  }

}
