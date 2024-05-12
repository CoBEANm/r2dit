package com.bork.r2dit.entity;

import jakarta.persistence.*;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private R2User user;

    @Column(nullable = false)
    private String title;

    @Column
    private String content;

    public Post() {
    }

    public Post(String title, String content, R2User userId) {
        this.title = title;
        this.content = content;
        this.user = userId;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUser() {
        return user.getName();
    }

    public String getContent() {
        return content;
    }
}
