package com.wuying;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//@EnableFeignClients(basePackages = "com.wuying.common.feign.clients")
@SpringBootApplication

public class AuthorizationServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }



}
