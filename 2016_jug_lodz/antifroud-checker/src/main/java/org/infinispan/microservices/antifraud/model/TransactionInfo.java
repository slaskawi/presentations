package org.infinispan.microservices.antifraud.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Currency;

public class TransactionInfo {

   private final String transactionId;
   private final ZonedDateTime transactionTime;
   private final BigDecimal amount;
   private final Currency currency;
   private final String ip;

   public TransactionInfo(String transactionId, ZonedDateTime transactionTime, BigDecimal amount, Currency currency, String ip) {
      this.transactionId = transactionId;
      this.transactionTime = transactionTime;
      this.amount = amount;
      this.currency = currency;
      this.ip = ip;
   }

   public String getTransactionId() {
      return transactionId;
   }

   public ZonedDateTime getTransactionTime() {
      return transactionTime;
   }

   public BigDecimal getAmount() {
      return amount;
   }

   public Currency getCurrency() {
      return currency;
   }

   public String getIp() {
      return ip;
   }

   @Override
   public String toString() {
      return "TransactionInfo{" +
            "transactionId='" + transactionId + '\'' +
            ", transactionTime=" + transactionTime +
            ", amount=" + amount +
            ", currency=" + currency +
            ", ip='" + ip + '\'' +
            '}';
   }
}
