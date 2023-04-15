package com.example.demo.config.auth;


//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인이 진행이 완료가 되면 security session을 만들어줌
// 세션은 세션인데 이건 시큐리티가 자신만의 세션을 만들어줌 - (Security ContextHolder)
// 세션에 들어갈 수 있는 객체는 Authentication 객체로 정해져있음
// Authentication 안에는 유저정보가 있어야함.
// User 오브젝트 타입은 => userDetails 타입 객체

import com.example.demo.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// Security Session => Authentication => UserDetails
// 세션안에 들어갈수있는건 어쎈티케이션 객체고 그안에서 유저정보를 저장하는건 UserDetails 객체
//Authentication 객체는 PrincipalDetailsService 에서 만듬
public class PrincipalDetails implements UserDetails { //이렇게 하면 Authentication 객체안에 넣을수잇음

    private User user; //- 콤포지션

    public PrincipalDetails(User user) {
        this.user = user;
    }

    //해당 유저의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        // 우리사이트가 1년동안 회원이 로그인을 안하면 휴면계정이 되기로 했다면
        // 마지막 현재시간 - 마지막 로그인시간 > 1년 이면 false;
        return true;
    }
}


