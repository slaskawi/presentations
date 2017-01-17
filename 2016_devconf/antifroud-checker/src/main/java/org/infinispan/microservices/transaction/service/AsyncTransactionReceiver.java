package org.infinispan.microservices.transaction.service;

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
import org.infinispan.microservices.transaction.model.Transaction;
import org.springframework.stereotype.Service;

@Service
@ClientListener
public class AsyncTransactionReceiver {

   public static final String CACHE_FOR_TRANSACTIONS = "transactions_for_antifraud_check";

   private final RemoteCache<String, Transaction> remoteCache;

   private ExecutorService executorService = Executors.newFixedThreadPool(100);
   private final BlockingQueue<CompletableFuture<Transaction>> transactionQueue = new LinkedBlockingQueue<>();

   public AsyncTransactionReceiver(RemoteCacheManager remoteCacheManager) {
      this.remoteCache = remoteCacheManager.getCache(CACHE_FOR_TRANSACTIONS);
   }

   @PostConstruct
   public void init() {
      remoteCache.addClientListener(this);

      CloseableIterator<Map.Entry<Object, Object>> entryCloseableIterator1 = remoteCache.retrieveEntries(null, 50);
      while(entryCloseableIterator1.hasNext()) {
         Map.Entry<Object, Object> next = entryCloseableIterator1.next();
         try {
            transactionQueue.put(CompletableFuture.supplyAsync(() -> (Transaction) next.getValue(), executorService));
         } catch (InterruptedException e) {
            return;
         }
      }
   }

   @PreDestroy
   public void cleanup() {
      remoteCache.removeClientListener(this);
   }

   @ClientCacheEntryCreated
   public void receiveEntryAsync(ClientCacheEntryCreatedEvent event) throws InterruptedException {
      transactionQueue.put(CompletableFuture.supplyAsync(() -> remoteCache.get(event.getKey()), executorService));
   }

   public BlockingQueue<CompletableFuture<Transaction>> getTransactionQueue() {
      return transactionQueue;
   }

}
