package cs6650.neu.a3.mysql;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import com.google.gson.Gson;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import org.apache.commons.dbcp2.*;

public class WordCountDao {

  private static final Logger logger = LogManager.getLogger(WordCountDao.class.getName());
  private static BasicDataSource dataSource;

  public WordCountDao() {
    dataSource = DBCPDataSource.getDataSource();
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
          " wordCount int, " +
          " primary key (wordKey));";
      stmt.executeUpdate(createQueryStatement);
      logger.info("Created table in given database... : " + tableName);
    } catch (SQLException e) {
      logger.info(e.getMessage());
    }
  }

  public void createWordCount(String tableName, String message) {
    Connection conn = null;
    String insertQueryStatement =
          "INSERT INTO " + tableName + "(wordKey, wordCount) values " + message;
    try {
      conn = dataSource.getConnection();
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(insertQueryStatement);
    } catch (SQLException e) {
      logger.info(e.getMessage());
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException e) {
        logger.info(e.getMessage());
      }
    }
  }

  public String processMessage(String message) {
    StringBuilder processedMessage = new StringBuilder();
    Gson gson = new Gson();
    JsonElement jsonElement = gson.fromJson(message, JsonElement.class);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
      String key = entry.getKey().replaceAll("'", "");
      String value = entry.getValue().getAsString();
      processedMessage.append("('").append(key).append("',").append(value).append("),");
    }
    processedMessage.deleteCharAt(processedMessage.length() - 1);
    processedMessage.append(";");
    return processedMessage.toString();
  }

}
