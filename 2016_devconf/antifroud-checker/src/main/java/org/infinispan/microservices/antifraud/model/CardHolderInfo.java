package org.infinispan.microservices.antifraud.model;

import java.time.LocalDate;

public class CardHolderInfo {

   private final String pan;
   private final String cardHolderFirstName;
   private final String cardHolderLastName;
   private final LocalDate expirationDate;

   public CardHolderInfo(String pan, String cardHolderFirstName, String cardHolderLastName, LocalDate expirationDate) {
      this.pan = pan;
      this.cardHolderFirstName = cardHolderFirstName;
      this.cardHolderLastName = cardHolderLastName;
      this.expirationDate = expirationDate;
   }

   public String getPan() {
      return pan;
   }

   public String getCardHolderFirstName() {
      return cardHolderFirstName;
   }

   public String getCardHolderLastName() {
      return cardHolderLastName;
   }

   public LocalDate getExpirationDate() {
      return expirationDate;
   }

   @Override
   public String toString() {
      return "CardHolderInfo{" +
            "pan='" + pan + '\'' +
            ", cardHolderFirstName='" + cardHolderFirstName + '\'' +
            ", cardHolderLastName='" + cardHolderLastName + '\'' +
            ", expirationDate=" + expirationDate +
            '}';
   }
}
