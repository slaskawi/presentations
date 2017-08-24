package org.infinispan.microservices;

import java.io.File;
import java.net.URISyntaxException;
import java.util.UUID;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

public class Main {

   private static final String SSL_REALM_1 = "client-1-truststore.jks";
   private static final String SSL_REALM_2 = "client-2-truststore.jks";

   public static void main(String[] args) throws Exception {

      ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
      configurationBuilder
            .addServers("transactions-repository-1-myproject.192.168.0.17.nip.io:443")
            .connectionPool()
               .maxTotal(1)
               .maxActive(1)
               .maxWait(0)
               .maxIdle(0)
               .maxRetries(0)
            .security()
               .ssl()
                  .enable()
                  .sniHostName("transactions-repository-1-myproject.192.168.0.17.nip.io")
                  .trustStoreFileName(getFilePath(SSL_REALM_2))
                  .trustStorePassword("secret".toCharArray())
      ;
      RemoteCacheManager remoteCacheManager = new RemoteCacheManager(configurationBuilder.build());

      boolean cacheAvailable1 = isCacheAvailable(remoteCacheManager, "cache-1");
      boolean cacheAvailable2 = isCacheAvailable(remoteCacheManager, "cache-2");

      Thread.sleep(100);

      System.out.println("cache-1: " + cacheAvailable1);
      System.out.println("cache-2: " + cacheAvailable2);
   }

   public static boolean isCacheAvailable(RemoteCacheManager remoteCacheManager, String cacheName) {
      try {
         remoteCacheManager.getCache(cacheName).put(UUID.randomUUID().toString(), "1");
         return true;
      } catch (Exception e) {
         return false;
      }
   }

   public static String getFilePath(String fieName) throws URISyntaxException {
      return new File(Main.class.getClassLoader().getResource(fieName).toURI()).getAbsolutePath();
   }
}
