package org.infinispan.microservices.transaction.transaction.integration.kafka.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import kafka.admin.AdminUtils;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;

@Configuration
public class KafkaConfiguration {

   @Value("${spring.kafka.url}")
   private String bootstrapServers;

   @Value("${spring.zookeeper.url}")
   private String zookeeperUrl;

   @Bean
   public KafkaTemplate kafkaTemplate() {
      Map props = new HashMap<>();
      // list of host:port pairs used for establishing the initial connections
      // to the Kakfa cluster
      props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
      props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
      props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
      props.put(ProducerConfig.BATCH_SIZE_CONFIG, 1);
      props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 100);

      ZkClient zkClient = new ZkClient(zookeeperUrl, 10 * 1000, 8 * 1000, ZKStringSerializer$.MODULE$);
      ZkUtils zkUtils = new ZkUtils(zkClient, new ZkConnection(zookeeperUrl), false);
      AdminUtils.createTopic(zkUtils, "transactions", 1, 1, new Properties());
      zkClient.close();

      KafkaTemplate kafkaTemplate = new KafkaTemplate(new DefaultKafkaProducerFactory<>(props));
      return kafkaTemplate;
   }

}
