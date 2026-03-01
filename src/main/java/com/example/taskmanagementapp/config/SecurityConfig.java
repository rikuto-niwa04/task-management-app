package com.example.taskmanagementapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 認可ルール
            .authorizeHttpRequests(auth -> auth
                // CSS/JSなど静的ファイルはログイン不要（Thymeleaf/Bootstrap用）
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
            )
            
            .formLogin(form -> form
                .defaultSuccessUrl("/tasks", true) // ← ここがポイント（trueで必ず/tasks）
                .permitAll()
            )
            
            .logout(Customizer.withDefaults());

        return http.build();
    }
}
