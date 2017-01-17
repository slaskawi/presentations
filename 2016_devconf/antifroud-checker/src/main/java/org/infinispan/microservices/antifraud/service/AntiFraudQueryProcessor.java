package org.infinispan.microservices.antifraud.service;

import java.util.function.Function;

import org.infinispan.microservices.antifraud.model.AntiFraudQueryData;
import org.infinispan.microservices.antifraud.model.AntiFraudResponseData;
import org.springframework.stereotype.Service;

@Service
public class AntiFraudQueryProcessor {

   public Function<AntiFraudQueryData, AntiFraudResponseData> process() {
      return query -> {
         System.out.println(query);
         return new AntiFraudResponseData("1", 1);
      };
   }

}
