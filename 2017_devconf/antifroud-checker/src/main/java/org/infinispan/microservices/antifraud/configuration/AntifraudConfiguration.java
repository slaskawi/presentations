package org.infinispan.microservices.antifraud.configuration;

import org.infinispan.microservices.antifraud.service.AntiFraudQueryProcessor;
import org.infinispan.microservices.antifraud.service.GeoIpService;
import org.infinispan.microservices.antifraud.service.rules.impl.CardExpirationDateRule;
import org.infinispan.microservices.antifraud.service.rules.impl.IpRule;
import org.infinispan.microservices.antifraud.service.rules.impl.TransactionAmountRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AntifraudConfiguration {

   @Bean
   public GeoIpService geoIpService() {
      return new GeoIpService();
   }

   @Bean
   public AntiFraudQueryProcessor antiFraudQueryProcessor(GeoIpService geoIpService) {
      return new AntiFraudQueryProcessor(new TransactionAmountRule(), new IpRule(geoIpService), new CardExpirationDateRule());
   }

}
