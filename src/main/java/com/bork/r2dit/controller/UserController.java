package com.bork.r2dit.controller;

import com.bork.r2dit.entity.R2User;
import com.bork.r2dit.repository.R2UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController{

    @Autowired
    R2UserRepository userRepository;

    @Autowired
    HomeController homeController;

    @GetMapping("/user/profile/{id}")
    public String userProfile(@PathVariable long id, Model model) {
        homeController.home(model);

        R2User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        model.addAttribute("posts", user.getPosts());

        return "userProfile";
    }

}
