package org.infinispan.microservices.pipelines;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.infinispan.microservices.antifraud.service.AntiFraudQueryProcessor;
import org.infinispan.microservices.caching.CacheInspector;
import org.infinispan.microservices.transactions.model.Transaction;
import org.infinispan.microservices.transactions.service.AntiFraudResultSender;
import org.infinispan.microservices.transactions.service.AntifraudQueryMapper;
import org.infinispan.microservices.transactions.service.AsyncTransactionReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GridToGridPipeline {

   @Autowired
   private AsyncTransactionReceiver transactionReceiver;

   @Autowired
   private AntifraudQueryMapper queryMapper;

   @Autowired
   private AntiFraudQueryProcessor processor;

   @Autowired
   private AntiFraudResultSender resultSender;

   @Autowired
   private CacheInspector cacheInspector;

   public void processData() throws InterruptedException, ExecutionException {
      while(true) {
         CompletableFuture<Transaction> take = transactionReceiver.getTransactionQueue().take();
               take.thenApply(transaction -> queryMapper.toAntiFraudQuery(transaction))
               .thenApply(antiFraudQueryData -> processor.process(antiFraudQueryData))
               .thenAccept(antiFraudResponseData -> {
                  cacheInspector.printOutCacheContent();
                  resultSender.sendResults(antiFraudResponseData);
               }).get();
      }
   }

}
