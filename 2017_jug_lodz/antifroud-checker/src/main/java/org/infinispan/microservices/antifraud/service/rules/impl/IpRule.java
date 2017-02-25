package org.infinispan.microservices.antifraud.service.rules.impl;


import java.util.Optional;

import org.infinispan.microservices.antifraud.model.AntiFraudQueryData;
import org.infinispan.microservices.antifraud.service.GeoIpService;
import org.infinispan.microservices.antifraud.service.rules.AntiFraudRule;

public class IpRule implements AntiFraudRule {

   private final GeoIpService geoIpService;

   public IpRule(GeoIpService geoIpService) {
      this.geoIpService = geoIpService;
   }

   @Override
   public int process(AntiFraudQueryData query) {
      Optional<String> usersCountry = query.getUserInfo().map(u -> u.getCountry());
      Optional<String> transactionCountry = Optional.ofNullable(geoIpService.getCountry(query.getTransactionInfo().getIp()));
      return usersCountry.equals(transactionCountry) ? -100 : 100;
   }
}
