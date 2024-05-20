package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/api/signup").permitAll() 
                    .requestMatchers("/api/login").permitAll() 
                   .requestMatchers("/api/coordinates").permitAll() 
                   .requestMatchers("/api/listImages").permitAll() 
                    .anyRequest().authenticated() 
            )
            .httpBasic()
            .and()
            .formLogin()
            .and()
            .cors()
            .and()
            .csrf().disable();

        return http.build();
    }
}


