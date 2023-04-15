package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨 // config에 등록할 필터가 등록이됨
public class SecurityConfig {

    @Bean
    protected SecurityFilterChain filterChain (HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.authorizeHttpRequests()
                .requestMatchers("/user/**").authenticated()
                .requestMatchers("/manager/**").hasAnyRole("ADMIN","MANAGER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()//여기부터는 만약 허가를 받지않은 페이지로 이동할 경우 로그인 페이지로 이동시키는 역할을 함
                .formLogin()
                .loginPage("/login");


        return http.build();

    }
}
