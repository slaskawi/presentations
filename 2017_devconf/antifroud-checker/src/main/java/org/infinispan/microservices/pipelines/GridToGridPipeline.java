package org.infinispan.microservices.pipelines;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.infinispan.microservices.antifraud.service.AntiFraudQueryProcessor;
import org.infinispan.microservices.caching.CacheInspector;
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

   private Executor e = Executors.newCachedThreadPool();

   public void processData() throws InterruptedException, ExecutionException {
      while(true) {
         transactionReceiver.getTransactionQueue().take()
               .thenApplyAsync(transaction -> queryMapper.toAntiFraudQuery(transaction), e)
               .thenApplyAsync(antiFraudQueryData -> processor.process(antiFraudQueryData), e)
               .thenAcceptAsync(antiFraudResponseData -> {
                  cacheInspector.printOutCacheContent();
                  resultSender.sendResults(antiFraudResponseData);
               }, e);
      }
   }

}
