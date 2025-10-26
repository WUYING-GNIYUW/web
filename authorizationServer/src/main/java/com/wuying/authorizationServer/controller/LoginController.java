package com.wuying.authorizationServer.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.wuying.common.util.Util;

@Controller
public class LoginController {
    @GetMapping("/getLoginPage")
    public ResponseEntity<Void> login() {
        String internalUri = "/internal_protected/" + Util.encodePath("/loginPage.html");
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Accel-Redirect", internalUri);

        return ResponseEntity.ok().headers(headers).build();
    }
}