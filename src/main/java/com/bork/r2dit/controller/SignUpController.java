package com.bork.r2dit.controller;

import com.bork.r2dit.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignUpController {

    private final SecurityConfig securityConfig;

    @Autowired
    public SignUpController(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(@RequestParam String username, @RequestParam String password) {
        securityConfig.createUser(username, password);
        return "redirect:/login";
    }
}