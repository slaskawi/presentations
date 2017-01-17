package org.infinispan.microservices.transaction.transaction.integration.infinispan;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryCreated;
import org.infinispan.client.hotrod.annotation.ClientListener;
import org.infinispan.client.hotrod.event.ClientCacheEntryCreatedEvent;
import org.infinispan.commons.util.CloseableIterator;
import org.infinispan.microservices.transaction.transaction.Transaction;
import org.springframework.stereotype.Service;

@Service
@ClientListener
public class AsyncTransactionReceiver {

   public static final String CACHE_FOR_TRANSACTIONS = "transactions_for_antifraud_check";

   private final RemoteCacheManager remoteCacheManager;
   private final RemoteCache<String, Transaction> remoteCache;

   private final BlockingQueue<CompletableFuture<Transaction>> transactionQueue = new LinkedBlockingQueue<>();

   private ExecutorService executorService = Executors.newFixedThreadPool(100);

   public AsyncTransactionReceiver(RemoteCacheManager remoteCacheManager) {
      this.remoteCacheManager = remoteCacheManager;
      this.remoteCache = remoteCacheManager.getCache(CACHE_FOR_TRANSACTIONS);
   }

   @PostConstruct
   public void init() {
      CloseableIterator<Map.Entry<Object, Object>> entryCloseableIterator1 = remoteCache.retrieveEntries(null, 50);
      while(entryCloseableIterator1.hasNext()) {
         Map.Entry<Object, Object> next = entryCloseableIterator1.next();
         try {
            transactionQueue.put(CompletableFuture.supplyAsync(() -> (Transaction) next.getValue(), executorService));
         } catch (InterruptedException e) {
            System.out.println("Finishing processing...");
         }
      }

      while(!transactionQueue.isEmpty()) {
         try {
            System.out.println(transactionQueue.take().get());
         } catch (InterruptedException e) {
            e.printStackTrace();  // TODO: Customise this generated block
         } catch (ExecutionException e) {
            e.printStackTrace();  // TODO: Customise this generated block
         }
      }
   }


   @ClientCacheEntryCreated
   public void test(ClientCacheEntryCreatedEvent event) throws InterruptedException {
      transactionQueue.put(CompletableFuture.supplyAsync(() -> (Transaction) event.getKey(), executorService));
   }

}
