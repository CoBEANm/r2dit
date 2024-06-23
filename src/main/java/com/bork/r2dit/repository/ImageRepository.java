package com.bork.r2dit.repository;

import com.bork.r2dit.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
