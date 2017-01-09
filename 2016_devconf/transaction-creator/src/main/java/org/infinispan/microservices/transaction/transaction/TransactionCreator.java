package org.infinispan.microservices.transaction.transaction;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.infinispan.microservices.transaction.transaction.integration.kafka.AsyncKafkaSender;
import org.infinispan.microservices.transaction.user.UserData;
import org.infinispan.microservices.transaction.user.UserGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TransactionCreator {

   private static final int POOL_SIZE = 16;
   private static final String TRANSACTION_TOPIC = "transactions";

   private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

   private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(POOL_SIZE);

   private final UserGrabber userGrabber;
   private final AsyncKafkaSender kafkaSender;

   public TransactionCreator(UserGrabber userGrabber, AsyncKafkaSender kafkaSender) {
      this.userGrabber = userGrabber;
      this.kafkaSender = kafkaSender;
   }

   @PostConstruct
   public void init() {
      scheduler.scheduleAtFixedRate(createTransaction(), 1, 1, TimeUnit.SECONDS);
   }

   private Runnable createTransaction() {
      return () -> {
         try {
            Optional<UserData> user = userGrabber.getRandomUser();
            if(user.isPresent()) {
               String pan = getRandomPan();
               String firstName = user.get().getFirstName();
               String lastName = user.get().getLastName();
               BigDecimal amount = new BigDecimal(ThreadLocalRandom.current().nextInt(0, 999999));
               Currency currency = getRandomCurrency();
               LocalDate cardExpirationDate = LocalDate.now().plusMonths(ThreadLocalRandom.current().nextInt(0, 512));

               String ip = getRandomIp();
               String country = getRandomCountry();

               Transaction transaction = new Transaction(pan, firstName, lastName, cardExpirationDate, amount, currency);
               transaction.setIp(ip);
               transaction.setCountry(country);
               kafkaSender.sendMessage(TRANSACTION_TOPIC, transaction);
               logger.info("Created transaction: {}", transaction);
            } else {
               logger.info("No user data for transaction. Waiting...");
            }
         } catch (Exception e) {
            logger.error("Creating transaction failed", e);
         }
      };
   }

   private String getRandomPan() {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      return random.nextInt(1000, 9999) + " **** **** " + random.nextInt(1000, 9999);
   }

   public Currency getRandomCurrency() {
      int maxElement = Currency.getAvailableCurrencies().size() - 1;
      int randomElementIndex = ThreadLocalRandom.current().nextInt(0, maxElement);
      Iterator<Currency> currencyIterator = Currency.getAvailableCurrencies().iterator();
      for(int i = 0; i < randomElementIndex; ++i) {
         currencyIterator.next();
      }
      return currencyIterator.next();
   }

   public String getRandomIp() {
      ThreadLocalRandom r = ThreadLocalRandom.current();
      if(r.nextInt(0, 100) <= 50) {
         return r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
      }
      return null;
   }

   public String getRandomCountry() {
      ThreadLocalRandom r = ThreadLocalRandom.current();
      String [] countries = new String[] {"PL", "CZ", "UK", "US", null};
      return countries[r.nextInt(0, countries.length)];
   }
}
