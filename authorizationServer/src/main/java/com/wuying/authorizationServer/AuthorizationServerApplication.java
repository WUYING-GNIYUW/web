package com.wuying.authorizationServer;

import com.wuying.common.pojo.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

//@EnableFeignClients(basePackages = "com.wuying.common.feign.clients")
@SpringBootApplication(scanBasePackageClasses = {
        com.wuying.authorizationServer.AuthorizationServerApplication.class,
        com.wuying.common.CommonApplication.class
}
)
public class AuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }

}
