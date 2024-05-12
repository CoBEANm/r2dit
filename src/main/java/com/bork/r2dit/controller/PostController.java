package com.bork.r2dit.controller;

import com.bork.r2dit.entity.Post;
import com.bork.r2dit.entity.R2User;
import com.bork.r2dit.repository.PostRepository;
import com.bork.r2dit.repository.R2UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private R2UserRepository r2UserRepository;

    @GetMapping("/createPost")
    public String createPost() {
        return "createPost";
    }

    @PostMapping("/createPost")
    public String createPost(@RequestParam String title,@RequestParam String content) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        R2User user = r2UserRepository.findByUsername(auth.getName()).get();

        postRepository.save(new Post(title, content, user));
        return "redirect:/";
    }
}
