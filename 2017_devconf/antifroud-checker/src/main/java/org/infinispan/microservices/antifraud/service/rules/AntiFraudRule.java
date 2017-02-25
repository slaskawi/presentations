package org.infinispan.microservices.antifraud.service.rules;

import org.infinispan.microservices.antifraud.model.AntiFraudQueryData;

@FunctionalInterface
public interface AntiFraudRule {

   int process(AntiFraudQueryData query);

}
