package org.infinispan.microservices;

import org.infinispan.microservices.pipelines.GridToGridPipeline;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

   public static void main(String[] args) throws Exception {
      ConfigurableApplicationContext run = SpringApplication.run(Main.class, args);
      GridToGridPipeline pipeline = run.getBean(GridToGridPipeline.class);
      pipeline.processData();
   }
}
