package cs6650.neu.a3;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;

public class WordCount {
  public static String processMessage(String message) {
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

//  public static String processMessage(String message) {
//    return "('word1', 100), ('word2', 300), ('word3', 500);";
//  }

//  public static String processMessage(String message) {
//    StringBuilder processedMessage = new StringBuilder();
//    for (int i = 0; i < 20; i++) {
//      String key = "word" + Thread.currentThread().getId() + "i";
//      String value = "100";
//      processedMessage.append("('").append(key).append("',").append(value).append("),");
//    }
//    processedMessage.deleteCharAt(processedMessage.length() - 1);
//    processedMessage.append(";");
//    return processedMessage.toString();
//  }
}