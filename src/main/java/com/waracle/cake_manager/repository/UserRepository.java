package com.waracle.cake_manager.repository;

import com.waracle.cake_manager.model.UserDao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository // Let Spring this is a JPA repository class
public interface UserRepository extends CrudRepository<UserDao, Integer> {
    UserDao findByUsername(String username);
}
