package org.infinispan.microservices.transaction.service;

import org.infinispan.microservices.antifraud.model.AntiFraudResponseData;

public class AntiFraudResultSender {

   public void sendResults(AntiFraudResponseData resoonse) {
      System.out.println(resoonse);
   }

}
