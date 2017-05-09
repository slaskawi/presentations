package org.infinispan.microservices.transactions.service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.microservices.transactions.model.Transaction;

public class TransactionSender {

   public static final String CACHE_FOR_TRANSACTIONS = "transactions_for_antifraud_check";

   private final RemoteCacheManager remoteCacheManager;

   public TransactionSender(RemoteCacheManager remoteCacheManager) {
      this.remoteCacheManager = remoteCacheManager;
   }

   public void send(Transaction transaction) {
      RemoteCache<String, Transaction> transactionCache = remoteCacheManager.getCache(CACHE_FOR_TRANSACTIONS);
      Transaction previousObject = transactionCache.put(createKey(transaction), transaction);
      if(previousObject != null) {
         throw new IllegalArgumentException("Conflicted entries detected. O1: " + transaction + " O2: " + previousObject);
      }
   }

   public String createKey(Transaction transaction) {
      return Instant.now().toString() + "#" + DateTimeFormatter.ISO_ZONED_DATE_TIME.format(transaction.getTransactionTime()) + "#" + transaction.getExpirationDate().toString();
   }

}
