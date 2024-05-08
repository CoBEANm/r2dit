package com.bork.r2dit.entity;

import jakarta.persistence.*;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private R2User user;

    @Column(nullable = false)
    private String title;

    @Column
    private String content;

    public Post() {
    }

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
