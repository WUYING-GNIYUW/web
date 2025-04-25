package authorizationServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/logining")
    public String login() {
        return "/loginPage";
    }
}