package org.infinispan.microservices.transaction.service;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

import org.infinispan.microservices.antifraud.model.AntiFraudQueryData;
import org.infinispan.microservices.antifraud.model.CardHolderInfo;
import org.infinispan.microservices.antifraud.model.TransactionInfo;
import org.infinispan.microservices.antifraud.model.UserInfo;
import org.infinispan.microservices.transaction.model.Transaction;
import org.infinispan.microservices.user.service.UserGrabber;
import org.infinispan.microservices.util.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AntifraudQueryMapper {

   private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

   private final UserGrabber userGrabber;

   public AntifraudQueryMapper(UserGrabber userGrabber) {
      this.userGrabber = userGrabber;
   }

   @Timed
   public AntiFraudQueryData toAntiFraudQuery(Transaction transaction) {
      CardHolderInfo cardHolderInfo = new CardHolderInfo(transaction.getPan(), transaction.getCardHolderFirstName(), transaction.getCardHolderLastName(), transaction.getExpirationDate());
      TransactionInfo transactionInfo = new TransactionInfo(transaction.getCorrelationId(), transaction.getTransactionTime(), transaction.getAmount(), transaction.getCurrency());

      Optional<UserInfo> userInfo = userGrabber.getUser(cardHolderInfo.getCardHolderFirstName(), cardHolderInfo.getCardHolderLastName())
            .map(userData -> new UserInfo(userData.getEmail(), userData.getFirstName(), userData.getLastName(), userData.getCountry(), userData.getStreetName(), userData.getCity(), userData.getHouseNumber()));

      return new AntiFraudQueryData(cardHolderInfo, transactionInfo, userInfo);
   }
}
