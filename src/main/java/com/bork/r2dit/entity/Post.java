package com.bork.r2dit.entity;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private R2User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Vote> votes;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @Column(nullable = false)
    private String title;

    @Column
    private String content;

    public Post() {
    }

    public Post(String title, String content, R2User userId, Image image) {
        this.title = title;
        this.content = content;
        this.user = userId;
        this.image = image;
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
    public int getVotes() {
        return votes.stream().mapToInt(Vote::getValue).sum();
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public long getUserId() {
        return user.getId();
    }
    public long getImageId() {
        return image.getId();
    }
    public Image getImage() {
        return image;
    }
}
