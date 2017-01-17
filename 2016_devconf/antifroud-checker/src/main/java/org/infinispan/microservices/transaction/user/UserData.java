package org.infinispan.microservices.transaction.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class UserData {

   @Id
   @GeneratedValue
   private int id;
   private String email;
   private String firstName;
   private String lastName;
   private String country;
   private String streetName;
   private String city;
   private int houseNumber;

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getCountry() {
      return country;
   }

   public void setCountry(String country) {
      this.country = country;
   }

   public String getStreetName() {
      return streetName;
   }

   public void setStreetName(String streetName) {
      this.streetName = streetName;
   }

   public String getCity() {
      return city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public int getHouseNumber() {
      return houseNumber;
   }

   public void setHouseNumber(int houseNumber) {
      this.houseNumber = houseNumber;
   }

   @Override
   public String toString() {
      return "UserData{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", country='" + country + '\'' +
            ", streetName='" + streetName + '\'' +
            ", city='" + city + '\'' +
            ", houseNumber=" + houseNumber +
            '}';
   }
}
