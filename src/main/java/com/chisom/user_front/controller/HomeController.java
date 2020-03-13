package com.chisom.user_front.controller;

import com.chisom.user_front.domain.PrimaryAccount;
import com.chisom.user_front.domain.SavingsAccount;
import com.chisom.user_front.domain.User;
import com.chisom.user_front.repository.RoleRepository;
import com.chisom.user_front.security.UserRole;
import com.chisom.user_front.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

//@RestController rest controller does not work for server side rendering
@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping("/")
    public String Home() {
        return "redirect:/index";
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup(Model model) {
        User user = new User();

        model.addAttribute("user", user);
        return "signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupPost(@ModelAttribute("user") User user, Model model) {
        //redefine this method later, move them all into services.
        if (userService.checkUserExists(user.getUsername(), user.getEmail())) {
            if (userService.checkEmailExists(user.getEmail())) {
                model.addAttribute("emailExists", true);
            }

            if (userService.checkUsernameExists(user.getUsername())) {
                model.addAttribute("usernameExists", true);
            }

            return "signup";
        } else {
            Set<UserRole> userRoles = new HashSet<>();
            userRoles.add(new UserRole(user, roleRepository.findByName("ROLE_USER")));

            userService.createUser(user, userRoles);
            return "redirect:/";
        }
    }

    @RequestMapping("/userFront")
    public String userFront(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        PrimaryAccount primaryAccount = user.getPrimaryAccount();
        SavingsAccount savingsAccount = user.getSavingsAccount();

        model.addAttribute("primaryAccount", primaryAccount);
        model.addAttribute("savingsAccount", savingsAccount);

        return "userFront";
    }
}
