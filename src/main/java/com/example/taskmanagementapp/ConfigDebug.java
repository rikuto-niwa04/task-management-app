package com.example.taskmanagementapp;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ConfigDebug {

    @Bean
    ApplicationRunner printConfig(Environment env) {
        return args -> {
            System.out.println("=== ACTIVE PROFILES ===");
            for (String p : env.getActiveProfiles()) System.out.println(" - " + p);

            System.out.println("=== DATASOURCE ===");
            System.out.println("url=" + env.getProperty("spring.datasource.url"));
            System.out.println("user=" + env.getProperty("spring.datasource.username"));

            System.out.println("=== JPA ===");
            System.out.println("ddl=" + env.getProperty("spring.jpa.hibernate.ddl-auto"));
        };
    }
}
