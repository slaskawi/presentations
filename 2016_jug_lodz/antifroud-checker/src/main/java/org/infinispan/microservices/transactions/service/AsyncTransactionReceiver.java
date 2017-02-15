package org.infinispan.microservices.transactions.service;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryCreated;
import org.infinispan.client.hotrod.annotation.ClientListener;
import org.infinispan.client.hotrod.event.ClientCacheEntryCreatedEvent;
import org.infinispan.commons.util.CloseableIterator;
import org.infinispan.microservices.transactions.model.Transaction;

@ClientListener
public class AsyncTransactionReceiver {

   public static final String CACHE_FOR_TRANSACTIONS = "transactions_for_antifraud_check";
   private static final int QUEUE_CAPACITY = 10;
   private static final int THREAD_POOL_SIZE = 10;
   private static final int BATCH_SIZE = 5;

   private final RemoteCache<String, Transaction> remoteCache;

   private ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
   private final BlockingQueue<CompletableFuture<Transaction>> transactionQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);

   public AsyncTransactionReceiver(RemoteCacheManager remoteCacheManager) {
      this.remoteCache = remoteCacheManager.getCache(CACHE_FOR_TRANSACTIONS);
   }

   @PostConstruct
   public void init() {
      remoteCache.addClientListener(this);
      executorService.submit(() -> loadOldTransactions());
   }

   private void loadOldTransactions() {
      CloseableIterator<Map.Entry<Object, Object>> entryCloseableIterator1 = remoteCache.retrieveEntries(null, BATCH_SIZE);
      while(entryCloseableIterator1.hasNext()) {
         Map.Entry<Object, Object> next = entryCloseableIterator1.next();
         if(next != null && next.getValue() != null) {
            try {
               Transaction transaction = (Transaction) next.getValue();
               transactionQueue.put(CompletableFuture.completedFuture(transaction));
               remoteCache.removeAsync(next.getKey());
            } catch (InterruptedException e) {
               return;
            }
         }
      }
   }

   @PreDestroy
   public void cleanup() {
      remoteCache.removeClientListener(this);
   }

   @ClientCacheEntryCreated
   public void receiveEntryAsync(ClientCacheEntryCreatedEvent event) throws InterruptedException {
      transactionQueue.put(CompletableFuture.supplyAsync(() -> {
         Transaction transaction = remoteCache.get(event.getKey());
         remoteCache.removeAsync(event.getKey());
         return transaction;
      }, executorService));
   }

   public BlockingQueue<CompletableFuture<Transaction>> getTransactionQueue() {
      return transactionQueue;
   }

}
