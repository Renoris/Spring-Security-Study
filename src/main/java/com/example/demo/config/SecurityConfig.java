package com.example.demo.config;

import com.example.demo.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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

@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalOauth2UserService principalOauth2UserService;

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
                .defaultSuccessUrl("/") // 로그인이 완료되면 메인페이지로 이동하게... -- 만약 특정 페이지로 갓는데 로그인을 요청해서 성공하면 다시 그 특정 페이지로 감
                    //강제로 로그인 페이지로 리다이렉트 된것들은 로그인이 완료되면 다시 가려고 했던 페이지로 돌아감
                .and()
                .oauth2Login()//oauth2 로그인을 실행할수 있게 되면
                .loginPage("/loginForm") // 이걸 넣든 안넣든 인증이 필요하면 어차피 위에서 로그인 페이지를 지정해줫으니 간다.
        // 다만 구글로그인이 완료된뒤의 후처리가 필요함
        // 1.코드받기(인증)- 정상적인 사용자
        // 2. 엑세스토큰(권한) - 사용자 정보에 접근할 권한
        // 3. 사용자 프로필 정보를 가져와서
        // 4-1. 그 정보를 토대로 회원가입을 자동으로 진행 시키기도 함
        // 4-2. 구글이 들어있는 사용자 정보가 부족하다면 (이메일,전화번호, 이름, 아이디 밖에없음)
        // - 추가적인정보(집주소, 등급) - 추가적인 회원가입 창이 나와서 회원가입을 해야함
        // TIP - 구글로그인이 완료가 되면 코드X, (액세스 토큰+ 사용자프로필정보(O))를 받기 때문에 OAUth클라이언트를 쓰면 편리함
                .userInfoEndpoint().userService(principalOauth2UserService);  // 4-1 자동진행을 해줄 클래스// 후처리





        return http.build();

    }
}
