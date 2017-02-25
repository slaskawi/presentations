package org.infinispan.microservices.transactions.configuration;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.microservices.transactions.service.TransactionCreator;
import org.infinispan.microservices.transactions.service.TransactionSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionConfiguration {

   @Bean
   public TransactionSender transactionSender(RemoteCacheManager remoteCacheManager) {
      return new TransactionSender(remoteCacheManager);
   }

   @Bean
   public TransactionCreator transactionCreator(TransactionSender transactionSender) {
      return new TransactionCreator(transactionSender);
   }

}
