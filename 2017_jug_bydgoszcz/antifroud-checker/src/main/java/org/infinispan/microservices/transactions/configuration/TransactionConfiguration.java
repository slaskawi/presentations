package org.infinispan.microservices.transactions.configuration;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.microservices.transactions.service.AntiFraudResultSender;
import org.infinispan.microservices.transactions.service.AntifraudQueryMapper;
import org.infinispan.microservices.transactions.service.AsyncTransactionReceiver;
import org.infinispan.microservices.user.service.UserGrabber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
public class TransactionConfiguration {

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
