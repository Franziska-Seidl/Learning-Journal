package com.academy.learning_journal_team3.controller;

import com.academy.learning_journal_team3.entity.User;
import com.academy.learning_journal_team3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/welcome")
    public String welcomePage(Model model, Authentication authentication){

        User currentUser = userService.getCurrentUser(authentication);
        if (authentication != null) {

            String firstName = currentUser.getFirstName();
            String lastName = currentUser.getLastName();
            String role = currentUser.getRole();

            model.addAttribute("role", role);

            System.out.println("firstName: " + firstName);
            System.out.println("lastName: " + lastName);
            System.out.println("role: " + role);
        }

        model.addAttribute("user", currentUser);
        return "welcome";
    }
}
