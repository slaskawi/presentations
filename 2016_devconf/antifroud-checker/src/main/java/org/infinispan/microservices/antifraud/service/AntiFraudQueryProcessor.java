package org.infinispan.microservices.antifraud.service;

import java.lang.invoke.MethodHandles;

import org.infinispan.microservices.antifraud.model.AntiFraudQueryData;
import org.infinispan.microservices.antifraud.model.AntiFraudResponseData;
import org.infinispan.microservices.util.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AntiFraudQueryProcessor {

   private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

   @Timed
   public AntiFraudResponseData process(AntiFraudQueryData query) {
      logger.info("Processing query {}", query);
      return new AntiFraudResponseData("1", 1);
   }

}
