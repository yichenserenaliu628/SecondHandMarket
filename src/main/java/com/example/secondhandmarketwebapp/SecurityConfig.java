package com.example.secondhandmarketwebapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

//https://docs.spring.io/spring-security/site/docs/5.5.5/reference/html5/

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;


    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin()
                .failureForwardUrl("/login?error=true");
        http
                .authorizeRequests()
                .antMatchers("/post/*", "/cart", "/checkout").hasAuthority("ROLE_USER")
                .anyRequest().permitAll();
    }


    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery("SELECT email, password, isEnabled FROM user WHERE id=?")
                .authoritiesByUsernameQuery("SELECT email, authorities FROM authorities WHERE id=?");

    }
}
