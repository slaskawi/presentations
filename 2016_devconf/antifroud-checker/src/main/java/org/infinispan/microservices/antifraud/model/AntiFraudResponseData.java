package org.infinispan.microservices.antifraud.model;

public class AntiFraudResponseData {

   private final String transactionId;
   private final int scoring;

   public AntiFraudResponseData(String transactionId, int scoring) {
      this.transactionId = transactionId;
      this.scoring = scoring;
   }

   @Override
   public String toString() {
      return "AntiFraudResponseData{" +
            "transactionId='" + transactionId + '\'' +
            ", scoring=" + scoring +
            '}';
   }
}
