package org.infinispan.microservices.transactions.service;

import java.util.Map;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;

public class TransactionSender {

   public static final String CACHE_FOR_TRANSACTIONS = "transactions";

   private final RemoteCache<String, String> transactionsCache;

   public TransactionSender(RemoteCacheManager remoteCacheManager) {
      transactionsCache = remoteCacheManager.getCache(CACHE_FOR_TRANSACTIONS);
   }

   public void send(Map<String, String> transactions) {
//      transactions.entrySet().forEach(e -> {
//         transactionsCache.put(e.getKey(), e.getValue());
//      });
      transactionsCache.putAll(transactions);
   }

}
