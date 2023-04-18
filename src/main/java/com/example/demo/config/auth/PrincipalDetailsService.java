package com.example.demo.config.auth;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login");
// /login 요청이오면 자동으로 UserDetailsService 타입을 IoC 되어있는 loadUserByusername 함수가 실행
// 물론 들어올때 username 이라고 와야함 - camelcase 잘못쓰면안됨
// 만약 그것을 바꾸고싶다면 SecurityConfig 주석 처리된부분 확인
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    //여기에서 되는 리턴은 securitySession => Authentication(내부)=> userDetails로 됨
    //이 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어짐
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return new PrincipalDetails(user);
        }
        return null;

        //이렇게 하면 로그인이 완료가... 됨!
    }
}
