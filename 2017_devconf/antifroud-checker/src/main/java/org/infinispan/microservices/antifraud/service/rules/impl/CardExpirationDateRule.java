package org.infinispan.microservices.antifraud.service.rules.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.infinispan.microservices.antifraud.model.AntiFraudQueryData;
import org.infinispan.microservices.antifraud.service.rules.AntiFraudRule;

public class CardExpirationDateRule implements AntiFraudRule {

   @Override
   public int process(AntiFraudQueryData query) {
      LocalDate expirationDate = query.getCardHolderInfo().getExpirationDate();
      LocalDate maximumDate = LocalDate.now().plus(3, ChronoUnit.YEARS);
      return expirationDate.isAfter(LocalDate.now()) && expirationDate.isBefore(maximumDate) ? -100 : 600;
   }
}
