package org.infinispan.microservices.transactions.service;

import java.lang.invoke.MethodHandles;
import java.util.Set;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionsGetter {

   private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

   private static final String CACHE_NAME = "transactions";

   private final RemoteCache remoteCache;

   public TransactionsGetter(RemoteCacheManager remoteCacheManager) {
      remoteCache = remoteCacheManager.getCache(CACHE_NAME);
   }

   public void getTransactionKeys() {
      Set keys = remoteCache.keySet();
      keys.forEach(key -> logger.info("Key: {}", key));
      logger.info("Total keys: {}", keys.size());
   }
}
