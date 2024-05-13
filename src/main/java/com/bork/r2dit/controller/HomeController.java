package com.bork.r2dit.controller;

import com.bork.r2dit.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
        model.addAttribute("posts", postRepository.findAll());

        if (name.equals("anonymousUser")) {
            model.addAttribute("username", "Not logged in");
            return "index";
        }

        model.addAttribute("username", name);
        return "index";
    }

    @Transactional
    @PostMapping("/")
    public String vote(@RequestParam long id, @RequestParam String vote) {
        if (vote.equals("up")) {
            postRepository.findById(id).ifPresent(post -> {
                post.setVotes(post.getVotes() + 1);
                postRepository.save(post);
            });
        } else {
            postRepository.findById(id).ifPresent(post -> {
                post.setVotes(post.getVotes() - 1);
                postRepository.save(post);
            });
        }


        return "redirect:/";
    }
}
