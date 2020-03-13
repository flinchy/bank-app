package com.chisom.user_front.service.UserServiceImpl;

import com.chisom.user_front.domain.User;
import com.chisom.user_front.repository.RoleRepository;
import com.chisom.user_front.repository.UserRepository;
import com.chisom.user_front.security.UserRole;
import com.chisom.user_front.service.AccountService;
import com.chisom.user_front.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void save(User user) {
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(User user, Set<UserRole> userRoles) {
        User localUser = userRepository.findByUsername(user.getUsername());

        if (localUser != null) {
            log.info("User with username {}, already exists. Nothing will be done", user.getUsername());
        } else {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);

            for (UserRole ur : userRoles) {
                roleRepository.save(ur.getRole());
            }

            user.getUserRoles().addAll(userRoles);

            user.setPrimaryAccount(accountService.createPrimaryAccount());
            user.setSavingsAccount(accountService.createSavingsAccount());

            localUser = userRepository.save(user);
        }
        return localUser;
    }

    public boolean checkUserExists(String username, String email) {
        if (checkUsernameExists(username) || checkEmailExists(email)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkUsernameExists(String username) {
        if (null != findByUsername(username)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkEmailExists(String email) {
        if (null != findByEmail(email)) {
            return true;
        } else {
            return false;
        }
    }
}
