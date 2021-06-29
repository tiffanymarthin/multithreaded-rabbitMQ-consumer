package cs6650.neu.a3.rmq;

import com.rabbitmq.client.Channel;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class PoolUtil {

  /**
   * Method to initialize RabbitMQ Channel pool using ChannelFactory class
   *
   * @return Channel Object Pool
   * @throws Exception when initialization fails
   */
  public static ObjectPool<Channel> initializePool() throws Exception {
//    GenericObjectPoolConfig config = new GenericObjectPoolConfig();
//    config.setMinIdle(3);
//    config.setMaxIdle(5);
//    config.setMaxTotal(20);
//    ObjectPool<Channel> pool = new GenericObjectPool<>(new ChannelFactory(), config);
//    return pool;
    return new GenericObjectPool<>(new ChannelFactory());
  }
}
