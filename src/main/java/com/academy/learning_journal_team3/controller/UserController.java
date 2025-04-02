package com.academy.learning_journal_team3.controller;

import com.academy.learning_journal_team3.entity.User;
import com.academy.learning_journal_team3.model.CustomUserDetails;
import com.academy.learning_journal_team3.service.PasswordResetService;
import com.academy.learning_journal_team3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping("/registration")
    public String registrationPage(Authentication authentication, Model model,
                                   @RequestParam(name = "error", defaultValue = "") String error) {
        boolean isUsers = !userService.getAllUsers().isEmpty();
        model.addAttribute("isUsers", isUsers);
        model.addAttribute("error",error);

        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User currentUser = userService.findByEmail(userDetails.getUsername());
            model.addAttribute("user", currentUser);
            model.addAttribute("adminCreation", false);
            model.addAttribute("editMode", true);
        } else {
            model.addAttribute("user", new User());
            model.addAttribute("adminCreation", false);
            model.addAttribute("editMode", false);
        }
        return "registration";
    }

    @PostMapping("/registration")
    public String userRegistration(@ModelAttribute User user, Authentication authentication) {
        if (userService.findByEmail(user.getEmail()) != null) {
            return "redirect:/registration?error=email_exists";
        }
        if (authentication != null &&
                authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) &&
                (user.getId() == null || user.getId() == 0)) {
            userService.saveUser(user);
            return "redirect:/admin/users";
        }

        userService.saveUser(user);
        return "redirect:/welcome";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            passwordResetService.sendPasswordResetEmail(email);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Ein temporäres Passwort wurde an Ihre E-Mail-Adresse gesendet.");
            return "redirect:/login";
        } catch (UsernameNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Es wurde kein Benutzer mit dieser E-Mail Adresse gefunden.");
            return "redirect:/forgot-password";
        }
    }

    @GetMapping("/profile")
    public String profilePage(Authentication authentication, Model model) {
        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User currentUser = userService.findByEmail(userDetails.getUsername());
            model.addAttribute("user", currentUser);
            return "profile";
        }
        return "redirect:/login";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User user, Authentication authentication) {
        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User currentUser = userService.findByEmail(userDetails.getUsername());

            boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

            if (isAdmin || currentUser.getId().equals(user.getId())) {
                userService.updateUser(user);

                if (isAdmin && !currentUser.getId().equals(user.getId())) {
                    return "redirect:/admin/users";
                }

                return "redirect:/profile?success";
            }
        }
        return "redirect:/error";
    }

    @PostMapping("/profile/delete")
    public String deleteProfile(@RequestParam Long userId, Authentication authentication) {
        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User currentUser = userService.findByEmail(userDetails.getUsername());

            boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

            if (isAdmin || currentUser.getId().equals(userId)) {
                userService.deleteUser(userId);

                if (currentUser.getId().equals(userId)) {
                    return "redirect:/logout";
                }

                if (isAdmin) {
                    return "redirect:/admin/users";
                }
            }
        }
        return "redirect:/error";
    }

    @GetMapping("/admin/users")
    public String userManagement(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping("/admin/users/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("adminCreation", true);
        model.addAttribute("editMode", false);
        return "registration";
    }

    @GetMapping("/admin/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("adminCreation", false);
            model.addAttribute("editMode", true);
            return "registration";
        }
        return "redirect:/admin/users";
    }
}
