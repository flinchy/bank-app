package com.chisom.user_front.repository;

import com.chisom.user_front.security.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByName(String name);
}
