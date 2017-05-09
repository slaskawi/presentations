package org.infinispan.microservices.user.service;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.fluttercode.datafactory.impl.DataFactory;
import org.infinispan.microservices.user.model.UserData;
import org.infinispan.microservices.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserCreator {

   private static final int NUMBER_OF_ENTRIES = 100;
   private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

   private final UserRepository userRepository;

   public UserCreator(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   @PostConstruct
   public void init() {
      logger.info("Initializing database");
      insertData();
      queryData();
      logger.info("Database initialization finished. Standing by...");
      while(true);
   }

   private void queryData() {
      userRepository.findAll().forEach(userData -> logger.info("User Data from database: {}", userData));
   }

   private void insertData() {
      DataFactory factory = new DataFactory();

      IntStream.range(0, NUMBER_OF_ENTRIES).forEach(i -> {

         UserData userData = new UserData();

         userData.setStreetName(factory.getStreetName());
         userData.setCity(factory.getCity());
         userData.setHouseNumber(factory.getNumberBetween(1, 100));
         userData.setCountry(factory.getItem(Arrays.asList("US", "GB"), 50));

         userData.setEmail(factory.getEmailAddress());
         userData.setFirstName(factory.getFirstName());
         userData.setLastName(factory.getLastName());

         userRepository.save(userData);
      });
   }
}
