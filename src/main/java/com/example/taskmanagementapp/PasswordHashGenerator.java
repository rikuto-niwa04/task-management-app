package com.example.taskmanagementapp;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "User1_pass_2026!";
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println(encodedPassword);
    }
}