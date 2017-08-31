package com.pethoalpar.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.pethoalpar.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUserName(String userName);
}
