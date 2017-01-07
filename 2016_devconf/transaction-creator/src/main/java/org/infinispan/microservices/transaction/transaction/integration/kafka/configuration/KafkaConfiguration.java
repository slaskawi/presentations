package org.infinispan.microservices.transaction.transaction.integration.kafka.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaConfiguration {

   @Value("${spring.kafka.url}")
   private String bootstrapServers;

   @Bean
   public KafkaTemplate kafkaTemplate() {
      Map props = new HashMap<>();
      // list of host:port pairs used for establishing the initial connections
      // to the Kakfa cluster
      props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
      props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
      props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
      props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000);

      KafkaTemplate kafkaTemplate = new KafkaTemplate(new DefaultKafkaProducerFactory<>(props));
      System.out.println("DUPA");
      return kafkaTemplate;
   }

}
