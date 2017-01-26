package org.infinispan.microservices.caching.configuration;

import org.infinispan.configuration.cache.CacheMode;
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
      //Configure Infinispan:
      // - Cluster name: Internal_cluster_for_caching
      // - DIST_SYNC caches: user_data, geo_ips

      GlobalConfigurationBuilder gcb = new GlobalConfigurationBuilder().clusteredDefault();
      gcb.transport().clusterName("Internal_cluster_for_caching");

      ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
      configurationBuilder.clustering().cacheMode(CacheMode.DIST_ASYNC);

      DefaultCacheManager defaultCacheManager = new DefaultCacheManager(gcb.build(), configurationBuilder.build());
      defaultCacheManager.defineConfiguration("user_data", configurationBuilder.build());
      defaultCacheManager.defineConfiguration("geo_ips", configurationBuilder.build());

      return new SpringEmbeddedCacheManager(defaultCacheManager);
   }

   @Bean
   public CacheInspector cacheInspector(SpringEmbeddedCacheManager springCache) {
      return new CacheInspector(springCache);
   }

}
