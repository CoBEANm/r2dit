package com.bork.r2dit.entity;

import jakarta.persistence.*;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @Column(name = "name")
    private String name;

    public long getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setName(String name) {
        this.name = name;
    }
}