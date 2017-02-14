package org.infinispan.microservices.antifraud.service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.infinispan.microservices.util.WaitUtils;
import org.springframework.cache.annotation.Cacheable;

public class GeoIpService {

   @Cacheable(cacheNames = "geo_ips")
   public String getCountry(String ip) {
      return getRandomCountry();
   }

   private String getRandomCountry() {
      WaitUtils.waitRandom(500, TimeUnit.MILLISECONDS);
      List<String> countries = Arrays.asList("GB", "US", null);
      return countries.get(ThreadLocalRandom.current().nextInt(0, countries.size()));
   }

}
