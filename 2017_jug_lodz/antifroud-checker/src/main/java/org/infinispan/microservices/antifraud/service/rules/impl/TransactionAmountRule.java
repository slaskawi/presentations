package org.infinispan.microservices.antifraud.service.rules.impl;


import java.math.BigDecimal;

import org.infinispan.microservices.antifraud.model.AntiFraudQueryData;
import org.infinispan.microservices.antifraud.service.rules.AntiFraudRule;

public class TransactionAmountRule implements AntiFraudRule {

   @Override
   public int process(AntiFraudQueryData query) {
      BigDecimal transactionAmount = query.getTransactionInfo().getAmount();
      BigDecimal threshold = new BigDecimal(100);
      return transactionAmount.compareTo(threshold) < 0 ? -50 : 50;
   }
}
