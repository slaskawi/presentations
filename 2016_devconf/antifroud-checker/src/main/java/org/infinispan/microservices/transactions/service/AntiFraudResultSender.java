package org.infinispan.microservices.transactions.service;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.microservices.antifraud.model.AntiFraudResponseData;
import org.infinispan.microservices.util.Timed;

public class AntiFraudResultSender {

   public static final String CACHE_FOR_RESULTS = "transactions_after_antifraud_check";

   private final RemoteCache<String, Integer> remoteCache;

   public AntiFraudResultSender(RemoteCacheManager remoteCacheManager) {
      this.remoteCache = remoteCacheManager.getCache(CACHE_FOR_RESULTS);
   }

   @Timed
   public void sendResults(AntiFraudResponseData resoonse) {
      remoteCache.putAsync(resoonse.getTransactionId(), resoonse.getScoring());
   }

}
