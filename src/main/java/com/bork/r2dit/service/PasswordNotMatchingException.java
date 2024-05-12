package com.bork.r2dit.service;

public class PasswordNotMatchingException extends RuntimeException{
    public PasswordNotMatchingException() {
        super("Passwords do not match. Please try again.");
    }
}
