package org.infinispan.microservices.caching;

import java.lang.invoke.MethodHandles;
import java.util.Set;

import org.infinispan.AdvancedCache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.spring.provider.SpringEmbeddedCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheInspector {

   private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

   private final SpringEmbeddedCacheManager springEmbeddedCacheManager;

   public CacheInspector(SpringEmbeddedCacheManager springEmbeddedCacheManager) {
      this.springEmbeddedCacheManager = springEmbeddedCacheManager;
   }

   public void printOutCacheContent() {
      EmbeddedCacheManager cacheManager = springEmbeddedCacheManager.getNativeCacheManager();
      Set<String> cacheNames = cacheManager.getCacheNames();
      StringBuilder sb = new StringBuilder();
      cacheNames.stream().forEach(cacheName -> {
         sb.append(cacheName).append("\n");
         printOutCacheContent(sb, cacheManager.getCache(cacheName).getAdvancedCache());
      });

      logger.debug("Cache Content \n{}", sb.toString());
   }

   public StringBuilder printOutCacheContent(StringBuilder stringBuilder, AdvancedCache cache) {
      cache.entrySet().stream()
            .forEach((c, e) -> stringBuilder.append("\t").append("->").append(e).append("\n"));
      return stringBuilder;
   }



}
