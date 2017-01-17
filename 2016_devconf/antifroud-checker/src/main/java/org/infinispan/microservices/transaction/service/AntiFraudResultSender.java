package org.infinispan.microservices.transaction.service;

import java.util.function.Consumer;

import org.infinispan.microservices.antifraud.model.AntiFraudResponseData;
import org.springframework.stereotype.Service;

@Service
public class AntiFraudResultSender {

   public Consumer<AntiFraudResponseData> sendResults() {
      return response -> {
         System.out.println(response);
      };
   }

}
