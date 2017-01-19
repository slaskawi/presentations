package org.infinispan.microservices.caching.configuration;

import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.microservices.caching.CacheInspector;
import org.infinispan.spring.provider.SpringEmbeddedCacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class SpringCacheConfiguration {

   @Bean
   public SpringEmbeddedCacheManager springCache() {
      GlobalConfigurationBuilder gcb = new GlobalConfigurationBuilder().clusteredDefault();
      ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

      DefaultCacheManager defaultCacheManager = new DefaultCacheManager(gcb.build(), configurationBuilder.build());
      defaultCacheManager.defineConfiguration("test", configurationBuilder.build());

      return new SpringEmbeddedCacheManager(defaultCacheManager);
   }

   @Bean
   public CacheInspector cacheInspector(SpringEmbeddedCacheManager springCache) {
      return new CacheInspector(springCache);
   }

}
