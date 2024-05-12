package com.bork.r2dit.controller;

import com.bork.r2dit.security.SecurityConfig;
import com.bork.r2dit.service.PasswordNotMatchingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    public String signUp(@RequestParam String username, @RequestParam String password, @RequestParam String password2, HttpServletRequest request) {
        if (!password.equals(password2)) {
            throw new PasswordNotMatchingException();
        }
        securityConfig.createUser(username, password);
        try {
            request.login(username, password);
        } catch (Exception e) {
            // User does not exist
        }
        return "redirect:/";
    }

    @ExceptionHandler(PasswordNotMatchingException.class)
    public String handleError(Model model, Throwable ex) {
        model.addAttribute("error", ex.getMessage());
        return "signup";
    }
}
