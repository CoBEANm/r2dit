package com.bork.r2dit.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class R2User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Vote> votes;


    @Column(nullable = false)
    private String password;

    public R2User() {
    }

    public R2User(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return username;
    }

    public List<Post> getPosts() {
        return posts;
    }
}
