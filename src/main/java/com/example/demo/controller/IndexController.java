package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
    public @ResponseBody String joinForm () {
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


    @GetMapping("/loginForm")
    public @ResponseBody String loginForm () {
        return "redirect:/loginForm"; //리다이렉트를 붙이면 해당 루트에맞는 함수를 호출해줌
    }
}
