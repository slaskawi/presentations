package org.infinispan.microservices.user.repository;

import org.infinispan.microservices.user.model.UserData;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserData, Integer> {

}
