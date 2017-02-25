package org.infinispan.microservices.antifraud.model;

import java.util.Optional;

public class AntiFraudQueryData {

   private final CardHolderInfo cardHolderInfo;
   private final TransactionInfo transactionInfo;
   private final Optional<UserInfo> userInfo;

   public AntiFraudQueryData(CardHolderInfo cardHolderInfo, TransactionInfo transactionInfo, Optional<UserInfo> userInfo) {
      this.cardHolderInfo = cardHolderInfo;
      this.transactionInfo = transactionInfo;
      this.userInfo = userInfo;
   }

   public CardHolderInfo getCardHolderInfo() {
      return cardHolderInfo;
   }

   public TransactionInfo getTransactionInfo() {
      return transactionInfo;
   }

   public Optional<UserInfo> getUserInfo() {
      return userInfo;
   }

   @Override
   public String toString() {
      return "AntiFraudQueryData{" +
            "cardHolderInfo=" + cardHolderInfo +
            ", transactionInfo=" + transactionInfo +
            ", userInfo=" + userInfo +
            '}';
   }
}
