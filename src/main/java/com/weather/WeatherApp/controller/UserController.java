package com.weather.WeatherApp.controller;

import com.weather.WeatherApp.model.User;
import com.weather.WeatherApp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 • Controller for user registration and login.
 */
@Controller
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService; // Inject UserService.
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // Add empty User object for form.
        return "register"; // Return registration form.
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        userService.register(user); // Register the user.
        return "redirect:/login"; // Redirect to login page.
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login"; // Return login form.
    }

}
