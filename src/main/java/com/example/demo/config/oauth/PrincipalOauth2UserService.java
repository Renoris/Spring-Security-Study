package com.example.demo.config.oauth;

import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.model.User;
import com.example.demo.provider.UserInfoProvider;
import com.example.demo.provider.userinfo.FacebookUserinfo;
import com.example.demo.provider.userinfo.GoogleUserinfo;
import com.example.demo.provider.userinfo.OAuth2UserInfo;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;


    //oauth2 후처리 하는 과정
    //이 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어짐
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest : " + userRequest);
        //userRequest가 들고잇는것
        //1. clientRegistration
        //- 서버에 대한 정보 - secret, scope, 구글 - 어떤 Oauth로 로그인 했는지 나옴

        //2. accessToken //
        //액세스토큰을 쓸일이없음 이미 다들고왔기때문에


        //3. super.loadUser(userRequest)
        //구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인 완료 => code를 리턴 (oauth-client라이브러리) -> accessToken 요청
        // userRequest정보 -> 회원 프로필을 받아야함(loaduser함수 호출) -> 회원 프로필
        // loadUser -> 구글로 부터 회원 프로필을 받아줌
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        // attribute - 사용자 프로필 정보가 다들어 있다
        //attribute안에 sub(고윳값) ,이름, 사진 url, locale,email, 이메일 만료됫는지?
        //그리고 username(userId)에 google_{sub}이렇게 하면 중복될 일이 없음
        //패스워드는 뭐 대충 암호화해서 넣으면..? null만 아니라면 상관없음
        // provider = 구글, 페북
        // providerId = provider 에서 판별하는 유저의 id,

        /*
        String provider = userRequest.getClientRegistration().getRegistrationId(); // google, facebook
        String providerId = oAuth2User.getAttribute("sub"); //google에서 보여주는 회원 아이디
        String providerId  = oAuth2User.getAttribute("id"); //facebook에서 보여주는 회원 아이디
        String userName = provider+"_"+providerId; //google_1231252345684
        String password = bCryptPasswordEncoder.encode("병준이짱");
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";
        */

        OAuth2UserInfo oAuth2UserInfo = UserInfoProvider.getOAuthUserInfo(userRequest, oAuth2User);

        String providerId = oAuth2UserInfo.getProviderId();
        String provider = oAuth2UserInfo.getProvider();
        String userName = provider+"_"+providerId;
        String password = bCryptPasswordEncoder.encode("병준이짱");
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        User user = userRepository.findByUsername(userName)
                .orElse(userRepository.save(
                        User.builder()
                                .username(userName)
                                .password(password)
                                .email(email)
                                .role(role)
                                .provider(provider)
                                .providerId(providerId)
                                .build()));


        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
