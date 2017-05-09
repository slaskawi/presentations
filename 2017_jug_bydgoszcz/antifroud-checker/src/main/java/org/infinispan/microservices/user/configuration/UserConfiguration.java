package org.infinispan.microservices.user.configuration;

import org.infinispan.microservices.user.repository.UserRepository;
import org.infinispan.microservices.user.service.UserGrabber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class UserConfiguration {

   @Bean
   public UserGrabber userGrabber(UserRepository userRepository) {
      return new UserGrabber(userRepository);
   }

}
