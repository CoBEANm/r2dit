package com.bork.r2dit.repository;

import com.bork.r2dit.entity.Post;
import com.bork.r2dit.entity.R2User;
import com.bork.r2dit.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Vote findByUserAndPost(R2User user, Post post);
}
