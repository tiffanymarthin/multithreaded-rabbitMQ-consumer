package cs6650.neu.a3;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import cs6650.neu.a3.rmq.PoolUtil;
import cs6650.neu.a3.sql.WordCountDao;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import org.apache.commons.pool2.ObjectPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

  private static final Logger logger = LogManager.getLogger(Main.class.getName());
  private static String TABLE_NAME = "wc1";
  private static String DELETE_TABLE = "delete";

  // Multi-threaded variables
  private static Integer MAX_THREADS = 128;
  private static ExecutorService executorService;

  // RabbitMQ variables
  private final static String QUEUE_NAME = "wcQueue";
  private final static Integer MAX_MESSAGE_PER_RECEIVER = 1;
  private static ObjectPool<Channel> rmqPool;

  public static void main(String[] args) throws Exception {
    if (args.length == 3) {
      TABLE_NAME = args[0];
      MAX_THREADS = Integer.parseInt(args[1]);
      DELETE_TABLE = args[2];
    }

    WordCountDao wordCountDao = new WordCountDao();

    Properties prop = new Properties();
    try (InputStream input = Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("config.properties")) {
      prop.load(input);
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    rmqPool = PoolUtil.initializePool();
//    ConnectionFactory factory = new ConnectionFactory();
//    factory.setUsername(prop.getProperty("rabbit.username"));
//    factory.setPassword(prop.getProperty("rabbit.password"));
//    factory.setHost(prop.getProperty("rabbit.ip"));
//
//    final Connection connection = factory.newConnection();

    executorService = Executors.newFixedThreadPool(MAX_THREADS);

    if (DELETE_TABLE.equals("delete")) {
      try {
        wordCountDao.deleteTable(TABLE_NAME);
        logger.info("delete table :" + TABLE_NAME);
        wordCountDao.createTable(TABLE_NAME);
      } catch (Exception e) {
        logger.info(e.getMessage());
      }
    }

    Runnable runnable = () -> {
      try {
//        final Channel channel = connection.createChannel();
        Channel channel = rmqPool.borrowObject();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // max one message per receiver
        channel.basicQos(MAX_MESSAGE_PER_RECEIVER);

        final DeliverCallback deliverCallback = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
          channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//          logger.info("Callback thread ID = " + Thread.currentThread().getId() + " Received: " + message);
          String processedMessage = WordCount.processMessage(message);
          wordCountDao.createWordCount(TABLE_NAME, processedMessage);
        };

        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
        });
        rmqPool.returnObject(channel);
      } catch (IOException e) {
        logger.info(e.getMessage());
      } catch (Exception e) {
        logger.info(e.getMessage());
        }
    };

    for (int i = 0; i < MAX_THREADS; i++) {
//    for (int i = 0; i < 1000; i++) {
      Thread thread = new Thread(runnable);
      thread.start();
//      executorService.submit(runnable);
    }

//    logger.info("Table is created");
//    if(test_insert == 1) {
//      logger.info("Attempt to insert some items to table...");
//      String message = "('word1', 100), ('word2', 300);";
//      try {
//        wordCountDao.createWordCount(TABLE_NAME, message);
//        logger.info("successful insert!");
//      } catch (Exception e) {
//        logger.info(e.getMessage());
//      }
//    }
  }
}
