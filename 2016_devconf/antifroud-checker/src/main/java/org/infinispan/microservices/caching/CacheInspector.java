package org.infinispan.microservices.caching;

import org.infinispan.spring.provider.SpringEmbeddedCacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheInspector {

   private final SpringEmbeddedCacheManager springEmbeddedCacheManager;

   public CacheInspector(SpringEmbeddedCacheManager springEmbeddedCacheManager) {
      this.springEmbeddedCacheManager = springEmbeddedCacheManager;
   }

   public void printOutCacheContent() {
      System.out.println(springEmbeddedCacheManager.getNativeCacheManager().getCache("test").entrySet());
   }

}
