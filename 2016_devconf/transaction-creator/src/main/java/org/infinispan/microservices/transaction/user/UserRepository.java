package org.infinispan.microservices.transaction.user;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserData, Integer> {

}
