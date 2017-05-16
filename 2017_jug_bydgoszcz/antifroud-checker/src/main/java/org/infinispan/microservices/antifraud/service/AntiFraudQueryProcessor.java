package org.infinispan.microservices.antifraud.service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.infinispan.microservices.antifraud.model.AntiFraudQueryData;
import org.infinispan.microservices.antifraud.model.AntiFraudResponseData;
import org.infinispan.microservices.antifraud.service.rules.AntiFraudRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntiFraudQueryProcessor {

   private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

   private final List<AntiFraudRule> rules = new ArrayList<>();

   public AntiFraudQueryProcessor(AntiFraudRule... rules) {
      this.rules.addAll(Arrays.asList(rules));
   }

   public AntiFraudResponseData process(AntiFraudQueryData query) {
      logger.info("Processing query {}", query.getTransactionInfo().getTransactionId());
      Integer scoring = rules.stream()
            .map(rule -> rule.process(query))
            .reduce(0, Integer::sum);
      return new AntiFraudResponseData(query.getTransactionInfo().getTransactionId(), scoring);
   }

}
