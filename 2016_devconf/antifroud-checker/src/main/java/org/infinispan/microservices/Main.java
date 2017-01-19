package org.infinispan.microservices;

import org.infinispan.microservices.pipelines.GridToGridPipeline;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

   public static void main(String[] args) throws InterruptedException {
      ConfigurableApplicationContext run = SpringApplication.run(Main.class, args);
      GridToGridPipeline bean = run.getBean(GridToGridPipeline.class);
      bean.processData();
   }
}
