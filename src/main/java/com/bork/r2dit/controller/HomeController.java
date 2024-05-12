package com.bork.r2dit.controller;

import com.bork.r2dit.repository.PostRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final PostRepository postRepository;

    public HomeController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        if (name.equals("anonymousUser")) {
            model.addAttribute("username", "Not logged in");
            return "index";
        }

        model.addAttribute("posts", postRepository.findAll());
        model.addAttribute("username", name);
        return "index";
    }

}
