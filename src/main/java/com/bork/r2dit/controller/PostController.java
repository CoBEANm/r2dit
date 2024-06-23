package com.bork.r2dit.controller;

import com.bork.r2dit.entity.Image;
import com.bork.r2dit.entity.Post;
import com.bork.r2dit.entity.R2User;
import com.bork.r2dit.repository.ImageRepository;
import com.bork.r2dit.repository.PostRepository;
import com.bork.r2dit.repository.R2UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class PostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private R2UserRepository r2UserRepository;
    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("/post/create")
    public String createPost(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        R2User user = r2UserRepository.findByUsername(name).get();

        model.addAttribute("user", user);
        return "createPost";
    }

    @PostMapping("/post/create")
    public String createPost(@RequestParam String title,@RequestParam String content, @RequestParam("image") MultipartFile image) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        R2User user = r2UserRepository.findByUsername(auth.getName()).get();

        Image postImage = new Image();
        try {
            postImage.setData(image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        postImage.setName(image.getOriginalFilename());
        imageRepository.save(postImage);


        Post post = new Post(title, content, user, postImage);
        postRepository.save(new Post(title, content, user, postImage));

        return "redirect:/";
    }

    @PreAuthorize("@securityService.isAuthor(#id)")
    @GetMapping("/post/edit/{id}")
    public String editPost(@PathVariable long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        model.addAttribute("post", post);
        return "editPost";
    }

    @PostMapping("/post/edit")
    public String editPost(@RequestParam String title, @RequestParam String content, @RequestParam long id) {
        postRepository.findById(id).ifPresent(post -> {
            post.setTitle(title);
            post.setContent(content);
            postRepository.save(post);
        });

        return "redirect:/";
    }
}
