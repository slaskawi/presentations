package org.infinispan.microservices.transactions.configuration;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.microservices.transactions.service.TransactionsGetter;
import org.infinispan.microservices.transactions.service.TransactionSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionConfiguration {

   @Bean
   public TransactionsGetter transactionCreator(RemoteCacheManager remoteCacheManager) {
      return new TransactionsGetter(remoteCacheManager);
   }

}
