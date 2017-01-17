package org.infinispan.microservices.transaction.configuration;

import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import infinispan.autoconfigure.remote.InfinispanRemoteConfigurer;

@Configuration
public class InfinispanConfiguration {

   @Value("${spring.infinispan.server}")
   private String infinispanAddress;

   @Bean
   public InfinispanRemoteConfigurer infinispanRemoteConfigurer() {
      return () -> new ConfigurationBuilder()
            .addServers(infinispanAddress)
            .build();
   }

}
