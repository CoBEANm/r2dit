package com.bork.r2dit.entity;

import jakarta.persistence.*;

@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private R2User user;

    @ManyToOne
    private Post post;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int value; // 1 for upvote, -1 for downvote

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setUser(R2User user) {
        this.user = user;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
