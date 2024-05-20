package com.bork.r2dit.security;

import com.bork.r2dit.entity.R2User;
import com.bork.r2dit.repository.R2UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {

    @Autowired
    private R2UserRepository R2UserRepository;


    @PreAuthorize("@securityService.isAuthor(#id)")
    public Optional<R2User> getUser(long id) {
        return R2UserRepository.findById(id);
    }

    public boolean isAuthor(long postId) {
        final var username = SecurityContextHolder.getContext().getAuthentication().getName();
        final var user = R2UserRepository
                .findByUsername(username);

        final var posts = user.get().getPosts();

        return posts.stream().anyMatch(post -> post.getId() == postId);
    }
}
