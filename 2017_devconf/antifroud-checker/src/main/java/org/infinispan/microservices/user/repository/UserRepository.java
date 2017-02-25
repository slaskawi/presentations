package org.infinispan.microservices.user.repository;

import java.util.List;

import org.infinispan.microservices.user.model.UserData;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserData, Integer> {

   List<UserData> findByFirstNameAndLastName(String firstName, String lastName);

}
