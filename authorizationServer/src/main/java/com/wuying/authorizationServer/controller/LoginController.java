package com.wuying.authorizationServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/getLoginPage")
    public String login() {
        return "loginPage";
    }
}