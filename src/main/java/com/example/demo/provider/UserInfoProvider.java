package com.example.demo.provider;

import com.example.demo.provider.userinfo.FacebookUserInfo;
import com.example.demo.provider.userinfo.GoogleUserInfo;
import com.example.demo.provider.userinfo.NaverUserinfo;
import com.example.demo.provider.userinfo.OAuth2UserInfo;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class UserInfoProvider {

    private static Map<String, String> oAuthIdMap;

    public static OAuth2UserInfo getOAuthUserInfo(OAuth2UserRequest userRequest, OAuth2User oAuth2User) throws OAuth2AuthenticationException {
        return switch (userRequest.getClientRegistration().getRegistrationId()) {
            case "google" -> new GoogleUserInfo(oAuth2User.getAttributes());
            case "facebook" -> new FacebookUserInfo(oAuth2User.getAttributes());
            //네이버는 response : {resultcode  =00. message = success, response : {id = 1771131346, email = ...}} 이런식으로 사실상 들어옴
            case "naver" -> new NaverUserinfo((Map)oAuth2User.getAttributes().get("response"));
            default -> throw new OAuth2AuthenticationException("우리는 구글과 페이스북과 네이버만 지원 해요 ㅎㅎㅎ");
        };

    }
}
