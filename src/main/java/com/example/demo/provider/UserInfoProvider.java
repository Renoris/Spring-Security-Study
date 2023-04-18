package com.example.demo.provider;

import com.example.demo.provider.userinfo.FacebookUserinfo;
import com.example.demo.provider.userinfo.GoogleUserinfo;
import com.example.demo.provider.userinfo.OAuth2UserInfo;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class UserInfoProvider {

    private static Map<String, String> oAuthIdMap;

    public static OAuth2UserInfo getOAuthUserInfo(OAuth2UserRequest userRequest, OAuth2User oAuth2User) throws OAuth2AuthenticationException {
        return switch (userRequest.getClientRegistration().getRegistrationId()) {
            case "google" -> new GoogleUserinfo(oAuth2User.getAttributes());
            case "facebook" -> new FacebookUserinfo(oAuth2User.getAttributes());
            default -> throw new OAuth2AuthenticationException("우리는 구글과 페이스북만 지원 해요 ㅎㅎㅎ");
        };

    }
}
