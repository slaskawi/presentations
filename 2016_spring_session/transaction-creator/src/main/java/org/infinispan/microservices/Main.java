package org.infinispan.microservices;

import java.util.concurrent.TimeUnit;

import org.infinispan.microservices.transactions.service.TransactionCreator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

   public static void main(String[] args) throws InterruptedException {
      ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
      TransactionCreator transactionCreator = context.getBean(TransactionCreator.class);
      transactionCreator.createTransactions();
      System.out.println("FINISHED");
      TimeUnit.DAYS.sleep(9999);
   }
}
