package org.infinispan.microservices.pipelines;

import java.util.concurrent.CompletableFuture;

import org.infinispan.microservices.antifraud.service.AntiFraudQueryProcessor;
import org.infinispan.microservices.caching.CacheInspector;
import org.infinispan.microservices.transaction.model.Transaction;
import org.infinispan.microservices.transaction.service.AntiFraudResultSender;
import org.infinispan.microservices.transaction.service.AntifraudQueryMapper;
import org.infinispan.microservices.transaction.service.AsyncTransactionReceiver;
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

   public void processData() throws InterruptedException {

      System.out.println("1");
      cacheInspector.testCaching();
      System.out.println("2");
      cacheInspector.testCaching();
      System.out.println("3");
      cacheInspector.testCaching();
      System.out.println("4");
      cacheInspector.testCaching();

      while(true) {
         CompletableFuture<Transaction> transactionToBeProcessed = transactionReceiver.getTransactionQueue().take();

         transactionToBeProcessed
               .thenApply(transaction -> queryMapper.toAntiFraudQuery(transaction))
               .thenApply(antiFraudQueryData -> processor.process(antiFraudQueryData))
               .thenAccept(antiFraudResponseData -> {
                  cacheInspector.printOutCacheContent();
                  resultSender.sendResults(antiFraudResponseData);
               });


      }
   }




}
