package org.infinispan.microservices.antifraud.model;

public class UserInfo {

   private final String email;
   private final String firstName;
   private final String lastName;
   private final String country;
   private final String streetName;
   private final String city;
   private final int houseNumber;

   public UserInfo(String email, String firstName, String lastName, String country, String streetName, String city, int houseNumber) {
      this.email = email;
      this.firstName = firstName;
      this.lastName = lastName;
      this.country = country;
      this.streetName = streetName;
      this.city = city;
      this.houseNumber = houseNumber;
   }

   public String getEmail() {
      return email;
   }

   public String getFirstName() {
      return firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public String getCountry() {
      return country;
   }

   public String getStreetName() {
      return streetName;
   }

   public String getCity() {
      return city;
   }

   public int getHouseNumber() {
      return houseNumber;
   }

   @Override
   public String toString() {
      return "UserInfo{" +
            "email='" + email + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", country='" + country + '\'' +
            ", streetName='" + streetName + '\'' +
            ", city='" + city + '\'' +
            ", houseNumber=" + houseNumber +
            '}';
   }
}
