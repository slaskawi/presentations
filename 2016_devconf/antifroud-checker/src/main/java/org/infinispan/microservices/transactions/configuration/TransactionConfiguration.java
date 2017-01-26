package org.infinispan.microservices.transactions.configuration;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.microservices.transactions.service.AntiFraudResultSender;
import org.infinispan.microservices.transactions.service.AntifraudQueryMapper;
import org.infinispan.microservices.transactions.service.AsyncTransactionReceiver;
import org.infinispan.microservices.transactions.service.AntiFraudResultSender;
import org.infinispan.microservices.transactions.service.AntifraudQueryMapper;
import org.infinispan.microservices.transactions.service.AsyncTransactionReceiver;
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
   public AntifraudQueryMapper antifraudQueryMapper(UserGrabber userGrabber) {
      return new AntifraudQueryMapper(userGrabber);
   }

   @Bean
   public AntiFraudResultSender antiFraudResultSender(RemoteCacheManager remoteCacheManager) {
      return new AntiFraudResultSender(remoteCacheManager);
   }

   @Bean
   public AsyncTransactionReceiver asyncTransactionReceiver(RemoteCacheManager remoteCacheManager) {
      return new AsyncTransactionReceiver(remoteCacheManager);
   }

}
