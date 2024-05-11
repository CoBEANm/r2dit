package com.bork.r2dit.controller;

import com.bork.r2dit.entity.R2User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.Authenticator;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        if (name.equals("anonymousUser")) {
            name = "Not logged in";
        }

        model.addAttribute("username", name);
        return "r2dit";
    }

}
