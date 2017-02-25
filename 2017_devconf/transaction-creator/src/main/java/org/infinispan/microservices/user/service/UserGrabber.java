package org.infinispan.microservices.user.service;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.Random;

import org.infinispan.microservices.user.model.UserData;
import org.infinispan.microservices.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserGrabber {

   private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

   private final UserRepository userRepository;

   public UserGrabber(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   public Optional<UserData> getRandomUser() {
      long totalUsers = userRepository.count();
      if(totalUsers == 0) {
         logger.info("Could not find any user data");
         return Optional.empty();
      }
      return Optional.ofNullable(userRepository.findOne(new Random().nextInt((int) totalUsers)));
   }

}
