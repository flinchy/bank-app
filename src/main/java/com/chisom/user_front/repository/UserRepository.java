package com.chisom.user_front.repository;

import com.chisom.user_front.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);
}
