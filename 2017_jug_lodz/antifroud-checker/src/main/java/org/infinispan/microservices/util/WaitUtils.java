package org.infinispan.microservices.util;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class WaitUtils {

   public static void waitRandom(int maxWaitTime, TimeUnit timeUnit) {
      try {
         timeUnit.sleep(ThreadLocalRandom.current().nextInt(0, maxWaitTime));
      } catch (InterruptedException e) {
         //ignore
      }
   }

}
