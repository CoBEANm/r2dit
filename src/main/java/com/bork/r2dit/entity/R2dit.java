package com.bork.r2dit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class R2dit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    
}
