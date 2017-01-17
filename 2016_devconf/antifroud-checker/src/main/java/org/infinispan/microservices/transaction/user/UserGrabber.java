package org.infinispan.microservices.transaction.user;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
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
