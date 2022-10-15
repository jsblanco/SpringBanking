package com.jsblanco.springbanking.config;

import com.jsblanco.springbanking.services.users.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and().build();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.csrf().disable();
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/").anonymous()
                .mvcMatchers(HttpMethod.POST, "/product/thirdparty").hasRole("THIRDPARTY")
                .mvcMatchers(HttpMethod.GET, "/thirdparty/**", "/holder/**").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.POST, "/thirdparty/**", "/holder/**", "/product/balance/**").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/thirdparty/**", "/holder/**").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.DELETE, "/thirdparty/**", "/holder/**").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.GET, "/product/**", "/student/**", "/checking/**", "/card/**", "/savings/**").hasAnyRole("ADMIN", "ACCOUNTHOLDER")
                .mvcMatchers(HttpMethod.POST, "/product/**", "/student/**", "/checking/**", "/card/**", "/savings/**").hasAnyRole("ADMIN", "ACCOUNTHOLDER")
                .mvcMatchers(HttpMethod.PUT, "/product/**", "/student/**", "/checking/**", "/card/**", "/savings/**").hasAnyRole("ADMIN", "ACCOUNTHOLDER")
                .mvcMatchers(HttpMethod.DELETE, "/product/**", "/student/**", "/checking/**", "/card/**", "/savings/**").hasAnyRole("ADMIN", "ACCOUNTHOLDER")
                .anyRequest().permitAll();

        return http.build();
    }
}
