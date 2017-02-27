package org.infinispan.microservices.user.service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.infinispan.microservices.user.model.UserData;
import org.infinispan.microservices.user.repository.UserRepository;
import org.infinispan.microservices.util.WaitUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserGrabber {

   private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

   private final UserRepository userRepository;

   public UserGrabber(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   //@Cacheable(cacheNames = "user_data")
   public Optional<UserData> getUser(String firstName, String lastName) {
      List<UserData> userDatas = userRepository.findByFirstNameAndLastName(firstName, lastName);

      WaitUtils.waitRandom(5, TimeUnit.SECONDS);

      if(userDatas.size() == 1) {
         return Optional.of(userDatas.get(0));
      } else if(userDatas.isEmpty()) {
         logger.warn("No username found in database for {} {}", firstName, lastName);
      } else {
         logger.warn("Found more than 1 user for card holder info {} {}. Skipping", firstName, lastName);
      }
      return Optional.empty();
   }

}
