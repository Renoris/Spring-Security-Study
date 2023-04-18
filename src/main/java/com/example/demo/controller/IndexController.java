package com.example.demo.controller;

import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //스프링 시큐리티는 자기만의 session을 들고 있음
    //그리고 세션안에 시큐리티가 관리하는 세션이 따로있음
    // 시큐리티가 관리하는 세션안에 들어갈수 있는 타입 - Authentication 객체
    // Authentication 안에 들어갈수 있는 타입
    // - UserDetails - 일반적인 로그인
    // - OAuth2User - OAuth 로그인
    // 컨트롤러에서 DI 가 힘든대...?
    // X라는 클래스를 하나 만들어서
    // 둘다 implement한 객체를 만들어서 X를 담아버리자
    // 근데 principalDetails 만들고 loadUser에서 썻는데?
    // 그럼 Oauth를 principalDetails 타입으로 묶어버리자

    @GetMapping("/test/login")
    public @ResponseBody String loginTest(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) {
        //@AuthenticationPrincipal 이라는 것으로 세션정보에 접근할수 있음
        //그런데 PrincipalDetails가 userDetails를 구현했기 때문에 PrincipalDetails 로도 받을수 잇음
        System.out.println("/test/login ==================");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); // 근데 이부분 다운캐스팅 할때 에러날거임
        //왜나하면 oauth로그인을 했을 경우 principalDetails에 OAuth2User 가 implementation 되어 있지 않는 이상 캐스팅이안댐
        //userDetails - 일반로그인
        System.out.println("authentication : "+ principalDetails.getUser());
        System.out.println("userDetails:"+ principalDetails.getUsername());

        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String loginOauthTest(Authentication authentication,
    @AuthenticationPrincipal OAuth2User oauth) {
        //@AuthenticationPrincipal 이라는 것으로 세션정보에 접근할수 있음
        System.out.println("/test/login ==================");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal(); // Oauth2User로 다운 캐스팅해야함
        // OAuth2User - Oauth 로그인에 쓰임 -
        System.out.println("authentication : "+ oAuth2User.getAttributes());
        System.out.println("oauth2User:"+ oauth.getAttributes());

        return "Oauth 세션 정보 확인하기";
    }

    @GetMapping({"","/"})
    public String index () {
        return "index";
    }

    @GetMapping("/joinForm")
    public String joinForm () {
        return "joinForm";
    }

    @PostMapping("/join")
    public @ResponseBody String join (User user) {
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user); // 회원가입은 잘되지만 => 시큐리티로 로그인을 할 수 없음 - 패스워드가 암호화되지않아서
        return "join";
    }

    // /logout 은 springSecurity default logout - session 초기화 해줌

    @GetMapping("/user")
    public @ResponseBody String user () {
        return "user";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager () {
        return "manager";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin () {
        return "admin";
    }


//    @GetMapping("/loginForm")
//    public @ResponseBody String loginForm () {
//        return "redirect:/loginForm"; //리다이렉트를 붙이면 해당 루트에맞는 함수를 호출해줌
//    }

    @GetMapping("/loginForm")
    public String loginForm () {
        return "loginForm"; //리다이렉트를 붙이면 해당 루트에맞는 함수를 호출해줌
    }

    @Secured("ROLE_ADMIN") // 특정 메소드에 간단하게 권한을 걸 수 있음
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    // secured는 권한 하나 만 걸수있지만 애는 hasRole로 여러개 걸수 있다.
//    @PostAuthorize()//함수가 끝나고 난뒤
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }
}
