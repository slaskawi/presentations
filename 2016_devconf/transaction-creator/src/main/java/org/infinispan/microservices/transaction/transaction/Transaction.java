package org.infinispan.microservices.transaction.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

//https://www.quora.com/Can-businesses-use-credit-card-data-for-customer-analytics
public class Transaction {

   private final String pan;
   private final String cardHolderFirstName;
   private final String cardHolderLastName;
   private final LocalDate expirationDate;

   private final BigDecimal amount;
   private final Currency currency;

   private String ip;
   private String country;

   public Transaction(String pan, String cardHolderFirstName, String cardHolderLastName, LocalDate expirationDate, BigDecimal amount, Currency currency) {
      this.pan = pan;
      this.cardHolderFirstName = cardHolderFirstName;
      this.cardHolderLastName = cardHolderLastName;
      this.expirationDate = expirationDate;
      this.amount = amount;
      this.currency = currency;
   }

   public void setIp(String ip) {
      this.ip = ip;
   }

   public void setCountry(String country) {
      this.country = country;
   }

   public String getPan() {
      return pan;
   }

   public LocalDate getExpirationDate() {
      return expirationDate;
   }

   public String getIp() {
      return ip;
   }

   public String getCountry() {
      return country;
   }

   public String getCardHolderFirstName() {
      return cardHolderFirstName;
   }

   public String getCardHolderLastName() {
      return cardHolderLastName;
   }

   public BigDecimal getAmount() {
      return amount;
   }

   public Currency getCurrency() {
      return currency;
   }

   @Override
   public String toString() {
      return "Transaction{" +
            "pan='" + pan + '\'' +
            ", cardHolderFirstName='" + cardHolderFirstName + '\'' +
            ", cardHolderLastName='" + cardHolderLastName + '\'' +
            ", expirationDate=" + expirationDate +
            ", amount=" + amount +
            ", currency=" + currency +
            ", ip='" + ip + '\'' +
            ", country='" + country + '\'' +
            '}';
   }
}
