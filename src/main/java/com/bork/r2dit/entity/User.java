package com.bork.r2dit.entity;

import jakarta.persistence.*;
import org.springframework.data.repository.cdi.Eager;

@Eager
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String username;

    @ManyToOne
    private String post;

    public User() {
    }

    public User(String username){this.username = username;}
}
