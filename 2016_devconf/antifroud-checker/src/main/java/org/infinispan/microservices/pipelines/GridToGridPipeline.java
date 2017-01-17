package org.infinispan.microservices.pipelines;

import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import org.infinispan.microservices.antifraud.service.AntiFraudQueryProcessor;
import org.infinispan.microservices.transaction.model.Transaction;
import org.infinispan.microservices.transaction.service.AntiFraudResultSender;
import org.infinispan.microservices.transaction.service.AntifraudQueryMapper;
import org.infinispan.microservices.transaction.service.AsyncTransactionReceiver;
import org.springframework.stereotype.Service;

@Service
public class GridToGridPipeline {

   private final AsyncTransactionReceiver transactionReceiver;
   private final AntifraudQueryMapper queryMapper;
   private final AntiFraudQueryProcessor processor;
   private final AntiFraudResultSender resultSender;

   public GridToGridPipeline(AsyncTransactionReceiver transactionReceiver, AntifraudQueryMapper queryMapper, AntiFraudQueryProcessor processor, AntiFraudResultSender resultSender) {
      this.transactionReceiver = transactionReceiver;
      this.queryMapper = queryMapper;
      this.processor = processor;
      this.resultSender = resultSender;
   }

   @PostConstruct
   public void processData() throws InterruptedException {
      while(true) {
         CompletableFuture<Transaction> transactionToBeProcessed = transactionReceiver.getTransactionQueue().take();
         transactionToBeProcessed
               .thenApply(queryMapper.toAntiFraudQuery())
               .thenApply(processor.process())
               .thenAccept(resultSender.sendResults());
      }
   }


}
