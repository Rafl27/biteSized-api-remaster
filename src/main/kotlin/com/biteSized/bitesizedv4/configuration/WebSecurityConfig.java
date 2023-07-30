package com.biteSized.bitesizedv4.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests( (authorize) -> authorize
                        .requestMatchers("/user").permitAll()
                        .requestMatchers("/user/login").permitAll()
                        .requestMatchers("/story").permitAll()
                        .requestMatchers("/story/user").permitAll()
                        .requestMatchers("/story/{id}").permitAll()
                        .requestMatchers("/story/{id}/comments").permitAll()
                        .requestMatchers("/comment/{parentCommentId}/replies").permitAll()
                        .requestMatchers("/story/{id}/upvote").permitAll()
                        .requestMatchers("/story/{id}/downvote").permitAll()
                        .requestMatchers("/story/{storyId}/completestory").permitAll()
                        .requestMatchers("/comment/{storyId}/allcomments").permitAll()
                ).csrf().disable();
        return http.build();
    }
}
