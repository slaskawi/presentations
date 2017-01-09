package org.infinispan.microservices.transaction.transaction.integration.kafka;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class AsyncKafkaSender {

   private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

   private final KafkaTemplate<String, Object> kafkaTemplate;

   public AsyncKafkaSender(KafkaTemplate<String, Object> kafkaTemplate) {
      this.kafkaTemplate = kafkaTemplate;
   }

   public void sendMessage(String topic, Object message) {
      ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("transactions", message);
      future.addCallback(success -> {}, failure -> {
         logger.error("Could not send message " + message + " to topic " + topic);
         logger.error("Error:", failure.getCause());
      });
   }
}
