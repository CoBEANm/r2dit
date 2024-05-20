package com.bork.r2dit.repository;

import com.bork.r2dit.entity.R2User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface R2UserRepository extends JpaRepository<R2User, Long> {

    Optional<R2User> findByUsername(String username);
    

}
