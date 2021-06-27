package cs6650.neu.a3.mysql;

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
      String createQueryStatement = "CREATE TABLE " + tableName +
          "(id INT NOT NULL AUTO_INCREMENT, " +
          " wordKey varchar(255), " +
          " wordCount int, " +
          " primary key (id));";
      stmt.executeUpdate(createQueryStatement);
      logger.info("Created table in given database... : " + tableName);
    } catch (SQLException e) {
      logger.info(e.getMessage());
    }
  }

  public void createWordCount(String key, String value, String tableName) {
    Connection conn = null;
//    PreparedStatement preparedStatement = null;
//    String insertQueryStatement = "INSERT INTO " + tableName + " (id, wordKey, wordCount) " +
//        "VALUES (?, ?, ?)";

//    String insertQueryStatement2 = "INSERT INTO " + tableName + " (id, wordKey, wordCount) ";
    String insertQueryStatement2 =
        "INSERT INTO " + tableName + " values (null, " + "'" + key + "', " + value + ")";

    try {
      conn = dataSource.getConnection();
      Statement stmt = conn.createStatement();
      String finalInsertQuery = insertQueryStatement2 + "VALUES (null, " + key + value + ");";
      stmt.executeUpdate(insertQueryStatement2);

//      preparedStatement = conn.prepareStatement(insertQueryStatement);
//      preparedStatement.setString(1, null);
//      preparedStatement.setString(2, key);
//      preparedStatement.setInt(3, value);
//
//      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
//        if (preparedStatement != null) {
//          preparedStatement.close();
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    }
  }

//  public void updateTable(String message, String tableName) {
//    Gson gson = new Gson();
//    JsonElement jsonElement = gson.fromJson(message, JsonElement.class);
//    JsonObject jsonObject = jsonElement.getAsJsonObject();
//    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
//      String key = entry.getKey().replace("'", "");
//      String value = entry.getValue().getAsString();
//      createWordCount(key, value, tableName);
//    }
//  }

}
