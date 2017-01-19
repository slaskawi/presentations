package org.infinispan.microservices.util;

import java.lang.invoke.MethodHandles;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class Timer {

   private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

   @Around("@annotation(org.infinispan.microservices.util.Timed)")
   public Object logServiceAccess(ProceedingJoinPoint pjp) throws Throwable {
      long time = System.currentTimeMillis();
      Object retVal = pjp.proceed();
      logger.info("Completed {} within {} ms", pjp.getSignature(), System.currentTimeMillis() - time);
      return retVal;
   }

}
