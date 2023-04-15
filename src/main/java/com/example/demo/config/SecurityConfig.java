package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨 // config에 등록할 필터가 등록이됨
//@EnableGlobalMethodSecurity //@Deprecated 예정
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
// securedEnabled = true - 시큐어 어노테이션 활성화 -
// @Secured("ROLE_ADMIN") // 특정 메소드에 간단하게 권한을 걸 수 있음
// prePostEnabled = true
// @preAuthorize, @postAuthorize 라는 어노테이션 활성화 - secured 가 이후에 나온거- 더많이쓰는듯?
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

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
                .loginPage("/loginForm")
//                .usernameParameter("userName") // 프론트엔드에서 도착하는 username 파라미터를 userName이라고 받고싶을때 바꿔주자
                .loginProcessingUrl("/login") // /login주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해줌
                .defaultSuccessUrl("/"); // 로그인이 완료되면 메인페이지로 이동하게... -- 만약 특정 페이지로 갓는데 로그인을 요청해서 성공하면 다시 그 특정 페이지로 감
                    //강제로 로그인 페이지로 리다이렉트 된것들은 로그인이 완료되면 다시 가려고 했던 페이지로 돌아감



        return http.build();

    }
}
