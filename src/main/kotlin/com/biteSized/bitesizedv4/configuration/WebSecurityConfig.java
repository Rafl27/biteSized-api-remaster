package com.biteSized.bitesizedv4.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:5173");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        http.
                cors(corsConfigurer -> corsConfigurer.configurationSource(request -> corsConfiguration))
                .authorizeHttpRequests( (authorize) -> authorize
                        .requestMatchers("/user").permitAll()
                        .requestMatchers("/user/signup").permitAll()
                        .requestMatchers("/user/info").permitAll()
                        .requestMatchers("/user/bio/{userId}").permitAll()
                        .requestMatchers("/user/info/story/{storyId}").permitAll()
                        .requestMatchers("/user/info/comment/{commentId}").permitAll()
                        .requestMatchers("/user/login").permitAll()
                        .requestMatchers("/story").permitAll()
                        .requestMatchers("/story/all").permitAll()
                        .requestMatchers("/story/user").permitAll()
                        .requestMatchers("/story/{id}").permitAll()
                        .requestMatchers("/story/{id}/comments").permitAll()
                        .requestMatchers("/comment/{parentCommentId}/replies").permitAll()
                        .requestMatchers("/story/{id}/upvote").permitAll()
                        .requestMatchers("/story/{id}/downvote").permitAll()
                        .requestMatchers("/story/{storyId}/total-up-down").permitAll()
                        .requestMatchers("/story/{storyId}/total-comments").permitAll()
                        .requestMatchers("/story/{storyId}/completestory").permitAll()
                        .requestMatchers("/comment/{storyId}/allcomments").permitAll()
                        .requestMatchers("/comment/{storyId}").permitAll()
                        .requestMatchers("/comment/{commentId}/upvote").permitAll()
                        .requestMatchers("/comment/{commentId}/downvote").permitAll()
                        .requestMatchers("/upload").permitAll()
                ).csrf().disable();
        return http.build();
    }
}
