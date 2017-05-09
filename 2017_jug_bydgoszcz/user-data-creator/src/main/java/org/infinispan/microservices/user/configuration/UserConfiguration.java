package org.infinispan.microservices.user.configuration;

import org.infinispan.microservices.user.repository.UserRepository;
import org.infinispan.microservices.user.service.UserCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfiguration {

   @Bean
   public UserCreator userCreator(UserRepository userRepository) {
      return new UserCreator(userRepository);
   }

}
