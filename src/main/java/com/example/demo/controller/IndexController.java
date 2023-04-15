package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @GetMapping({"","/"})
    public String index () {
        return "index";
    }

    @GetMapping("/join")
    public @ResponseBody String join () {
        return "join";
    }

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


    @GetMapping("/login")
    public @ResponseBody String login () {
        return "login";
    }
}
