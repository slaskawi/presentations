package org.infinispan.microservices;

import java.util.concurrent.TimeUnit;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.microservices.sessions.SessionsServlet;
import org.infinispan.spring.provider.SpringRemoteCacheManager;
import org.infinispan.spring.session.configuration.EnableInfinispanRemoteHttpSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableInfinispanRemoteHttpSession(cacheName = "sessions")
@EnableWebMvc
public class Main {

   public static void main(String[] args) throws Exception {
      ConfigurableApplicationContext run = SpringApplication.run(Main.class, args);
      TimeUnit.DAYS.sleep(1000);
   }

   /**
    * We need to to create a Spring Remote Cache Manager. It will be automated in the future: https://issues.jboss.org/browse/ISPN-7468
    */
   @Bean
   public SpringRemoteCacheManager springCache(RemoteCacheManager remoteCacheManager) {
      return new SpringRemoteCacheManager(remoteCacheManager);
   }

   /**
    * Spring Web does not honour <code>@WebServlet</code> annotations.
    *
    * @see <a href="http://stackoverflow.com/a/27788243/562699" >
    */
   @Bean
   public ServletRegistrationBean delegateServiceExporterServlet() {
      return new ServletRegistrationBean(new SessionsServlet(), "/sessions");
   }
}
