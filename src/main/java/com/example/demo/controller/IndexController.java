package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
