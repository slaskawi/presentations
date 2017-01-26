package org.infinispan.microservices.transactions.model;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

import org.infinispan.commons.marshall.AdvancedExternalizer;

//https://www.quora.com/Can-businesses-use-credit-card-data-for-customer-analytics
public class Transaction implements AdvancedExternalizer<Transaction> {

   private final String correlationId;

   private final ZonedDateTime transactionTime;

   private final String pan;
   private final String cardHolderFirstName;
   private final String cardHolderLastName;
   private final LocalDate expirationDate;

   private final BigDecimal amount;
   private final Currency currency;

   private final String ip;
   private final String country;

   public Transaction(String correlationId, ZonedDateTime transactionTime, String pan, String cardHolderFirstName, String cardHolderLastName, LocalDate expirationDate, BigDecimal amount, Currency currency, String ip, String country) {
      this.correlationId = correlationId;
      this.transactionTime = transactionTime;
      this.pan = pan;
      this.cardHolderFirstName = cardHolderFirstName;
      this.cardHolderLastName = cardHolderLastName;
      this.expirationDate = expirationDate;
      this.amount = amount;
      this.currency = currency;
      this.ip = ip;
      this.country = country;
   }

   public String getCorrelationId() {
      return correlationId;
   }

   public ZonedDateTime getTransactionTime() {
      return transactionTime;
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

   public BigDecimal getAmount() {
      return amount;
   }

   public Currency getCurrency() {
      return currency;
   }

   public String getIp() {
      return ip;
   }

   public String getCountry() {
      return country;
   }

   @Override
   public String toString() {
      return "Transaction{" +
            "correlationId='" + correlationId + '\'' +
            ", transactionTime=" + transactionTime +
            ", pan='" + pan + '\'' +
            ", cardHolderFirstName='" + cardHolderFirstName + '\'' +
            ", cardHolderLastName='" + cardHolderLastName + '\'' +
            ", expirationDate=" + expirationDate +
            ", amount=" + amount +
            ", currency=" + currency +
            ", ip='" + ip + '\'' +
            ", country='" + country + '\'' +
            '}';
   }

   @Override
   public Set<Class<? extends Transaction>> getTypeClasses() {
      Set<Class<? extends Transaction>> supportedTypes = new HashSet<>(1);
      supportedTypes.add(Transaction.class);
      return Collections.unmodifiableSet(supportedTypes);
   }

   @Override
   public Integer getId() {
      return 667;
   }

   @Override
   public void writeObject(ObjectOutput objectOutput, Transaction transaction) throws IOException {
      objectOutput.writeChars(correlationId);
      objectOutput.writeChars(transactionTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
      objectOutput.writeChars(pan);
      objectOutput.writeChars(cardHolderFirstName);
      objectOutput.writeChars(cardHolderLastName);
      objectOutput.writeChars(expirationDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
      objectOutput.writeChars(amount.toString());
      objectOutput.writeChars(currency.toString());
      objectOutput.writeChars(ip);
      objectOutput.writeChars(country);
   }

   @Override
   public Transaction readObject(ObjectInput objectInput) throws IOException, ClassNotFoundException {
      String correlationId = objectInput.readUTF();
      ZonedDateTime transactionTime = (ZonedDateTime) DateTimeFormatter.ISO_ZONED_DATE_TIME.parse(objectInput.readUTF());
      String pan = objectInput.readUTF();
      String firstName = objectInput.readUTF();
      String lastName = objectInput.readUTF();
      LocalDate expirationDate = (LocalDate) DateTimeFormatter.ISO_LOCAL_DATE.parse(objectInput.readUTF());
      BigDecimal amount = new BigDecimal(objectInput.readUTF());
      Currency currency = Currency.getInstance(objectInput.readUTF());
      String ip = objectInput.readUTF();
      String country = objectInput.readUTF();

      Transaction transaction = new Transaction(correlationId, transactionTime, pan, firstName, lastName, expirationDate, amount, currency, ip, country);

      return transaction;
   }
}
