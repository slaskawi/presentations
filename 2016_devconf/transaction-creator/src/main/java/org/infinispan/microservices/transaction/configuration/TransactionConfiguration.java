package org.infinispan.microservices.transaction.configuration;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.microservices.transaction.service.TransactionCreator;
import org.infinispan.microservices.transaction.service.TransactionSender;
import org.infinispan.microservices.user.service.UserGrabber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import infinispan.autoconfigure.remote.InfinispanRemoteConfigurer;

@Configuration
public class TransactionConfiguration {

   @Value("${spring.infinispan.server}")
   private String infinispanAddress;

   @Bean
   public InfinispanRemoteConfigurer infinispanRemoteConfigurer() {
      return () -> new ConfigurationBuilder()
            .addServers(infinispanAddress)
            .build();
   }

   @Bean
   public TransactionSender transactionSender(RemoteCacheManager remoteCacheManager) {
      return new TransactionSender(remoteCacheManager);
   }

   @Bean
   public TransactionCreator transactionCreator(UserGrabber userGrabber, TransactionSender transactionSender) {
      return new TransactionCreator(userGrabber, transactionSender);
   }

}
