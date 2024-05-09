package com.bork.r2dit.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class R2User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    @Column
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
}
