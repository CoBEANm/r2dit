package com.bork.r2dit.controller;

import com.bork.r2dit.entity.Post;
import com.bork.r2dit.entity.R2User;
import com.bork.r2dit.entity.Vote;
import com.bork.r2dit.repository.PostRepository;
import com.bork.r2dit.repository.R2UserRepository;
import com.bork.r2dit.repository.VoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class HomeController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private R2UserRepository userRepository;
    @Autowired
    private VoteRepository voteRepository;

    @GetMapping("/login")
    public String login(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        model.addAttribute("username", name.equals("anonymousUser") ? "Not logged in" : name);

        return "login";
    }

    @GetMapping("/error")
    public String error(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        model.addAttribute("username", name.equals("anonymousUser") ? "Not logged in" : name);
        return "error";
    }

    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        R2User user = null;
        if (!name.equals("anonymousUser")) {
            user = userRepository.findByUsername(name).get();
        }
        List<Post> posts = postRepository.findAll(Sort.by("id").descending());
        Map<Long, String> votes = new HashMap<>();
        if (user != null) {
            for (Post post : posts) {
                Vote vote = voteRepository.findByUserAndPost(user, post);
                if (vote != null) {
                    votes.put(post.getId(), vote.getValue() == 1 ? "up" : "down");
                }
            }
        }
        model.addAttribute("posts", posts);
        model.addAttribute("votes", votes);
        model.addAttribute("username", name.equals("anonymousUser") ? "Not logged in" : name);
        return "index";
    }

    @Transactional
    @PostMapping("/")
    public String vote(@RequestParam long id, @RequestParam String vote) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        R2User user = userRepository.findByUsername(username).get();
        Post post = postRepository.findById(id).get();

        Vote existingVote = voteRepository.findByUserAndPost(user, post);
        if (existingVote != null) {
            existingVote.setValue(vote.equals("up") ? 1 : -1);
        } else {
            Vote newVote = new Vote();
            newVote.setUser(user);
            newVote.setPost(post);
            newVote.setValue(vote.equals("up") ? 1 : -1);
            voteRepository.save(newVote);
        }

        return "redirect:/";
    }
}
