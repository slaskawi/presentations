package org.infinispan.microservices.antifraud.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.infinispan.microservices.util.WaitUtils;

public class GeoIpService {

   private final Map<String, String> cache = new HashMap<>();

   public String getCountry(String ip) {
      return cache.computeIfAbsent(ip, v -> getRandomCountry());
   }

   private String getRandomCountry() {
      WaitUtils.waitRandom(500, TimeUnit.MILLISECONDS);
      List<String> countries = Arrays.asList("GB", "US", null);
      return countries.get(ThreadLocalRandom.current().nextInt(0, countries.size()));
   }

}
