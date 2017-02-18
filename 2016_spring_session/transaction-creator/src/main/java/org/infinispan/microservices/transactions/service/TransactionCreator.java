package org.infinispan.microservices.transactions.service;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionCreator {

   private static final int TOTAL_SIZE_IN_BYTES = 800 * 1024 * 1024; //500 MB
   private static final int BATCH_SIZE = 1000;

   private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

   private final TransactionSender transactionSender;

   public TransactionCreator(TransactionSender transactionSender) {
      this.transactionSender = transactionSender;
   }

   public void createTransactions() {
      int accumulatedSizeInBytes = 0;
      int totalInsertedRecords = 0;
      do {
         Map<String, String> batch = new HashMap<>();
         for (int i = 0; i < BATCH_SIZE; ++i) {
            String key = UUID.randomUUID().toString();
            String value = UUID.randomUUID().toString();

            //200 is a semi fixed size offset
            accumulatedSizeInBytes += key.getBytes().length + 200;
            accumulatedSizeInBytes += value.getBytes().length + 200;
            batch.put(key, value);
         }
         transactionSender.send(batch);
         totalInsertedRecords += batch.size();
         logger.info("Sending batch (size: {}). Total data set (size in MB: {}, records: {})", batch.size(), (accumulatedSizeInBytes/(1014*1024)), totalInsertedRecords);
      } while(accumulatedSizeInBytes <= TOTAL_SIZE_IN_BYTES);
   }
}
