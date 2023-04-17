package com.example.demo.config;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    //oauth2 후처리 하는 과정
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest : " + userRequest);
        //userRequest가 들고잇는것
        //1. clientRegistration
        //2. accessToken
        //3. super.loadUser(userRequest) 가 주는 것들도 잇다
        //super.loadUser(userRequest)를 하게되고 거기서 attribute를 꺼내개 되면 사용자 프로필 정보가 다들어 있다
        System.out.println(super.loadUser(userRequest).getAuthorities());

        //액세스토큰을 쓸일이없음 이미 다들고왔기때문에
        //attribute안에 sub(고윳값) ,이름, 사진 url, locale,email, 이메일 만료됫는지?
        //그리고 username(userId)에 google_{sub}이렇게 하면 중복될 일이 없음
        //패스워드는 뭐 대충 암호화해서 넣으면..? null만 아니라면 상관없음
        // provider = 구글 //페북
        // providerId = subfmf sjgwk



        return super.loadUser(userRequest);
    }
}
