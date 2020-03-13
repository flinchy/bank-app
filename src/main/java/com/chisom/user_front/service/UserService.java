package com.chisom.user_front.service;

import com.chisom.user_front.domain.User;
import com.chisom.user_front.security.UserRole;

import java.util.Set;

public interface UserService {

    User findByUsername(String username);

    User findByEmail(String email);

    boolean checkUserExists(String username, String email);

    boolean checkUsernameExists(String username);

    boolean checkEmailExists(String email);

    void save(User user);

    User createUser(User user, Set<UserRole> userRoles);
}
