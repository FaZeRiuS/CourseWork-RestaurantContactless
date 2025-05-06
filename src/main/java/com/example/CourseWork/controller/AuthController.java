package com.example.CourseWork.controller;

import com.example.CourseWork.dto.UserRegistrationDto;
import com.example.CourseWork.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;

    @GetMapping("/login")
    public String login() {
        return "forward:/login.html";
    }

    @GetMapping("/register")
    public String register() {
        return "forward:/registration.html";
    }

    @PostMapping("/register")
    public String processRegistration(@RequestParam String email,
                                      @RequestParam String name,
                                      @RequestParam String password) {
        try {
            UserRegistrationDto dto = new UserRegistrationDto(name, email, password);

            userService.register(dto);

            return "redirect:/login.html?registered=true";
        } catch (RuntimeException e) {
            return "redirect:/register.html?error=exists";
        }
    }

    @GetMapping("/home")
    public String home() {
        return "forward:/home.html";
    }

    @GetMapping("/logout")
    public String logoutSuccess() {
        return "redirect:/login.html?logout=true";
    }
}
