package com.wuying.userServer.controller;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.wuying.common.pojo.Result;
import com.wuying.common.pojo.User;
import com.wuying.common.util.Util;
import com.wuying.userServer.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/user")
@Tag(name = "Foruser")
public class UserController {
    private final UserServiceImpl userServiceImpl;
    private final Environment env;

    @Operation(summary = "普通user请求")
    @GetMapping("/test/{id}")
    public Result<User> getByIdJustForTest(@PathVariable(value = "id") Long id) {
        User user = userServiceImpl.getById(id);

        return Result.<User>builder().data(user).build();
    }
    @GetMapping("/test")
    public String ForTest() {
        return "ok";
    }

    @PostMapping("/add")
    public Result<Boolean> addUser(@RequestBody User addUser) {
        return userServiceImpl.addUser(addUser);
    }
    @PostMapping("/register")
    public Result<Boolean> registerUser(@RequestBody User registerUser) {
        return userServiceImpl.addUser(registerUser);
    }
    @PostMapping("/get")
    public Result<List<User>> getUsers(@RequestBody User queryUser) {
        return userServiceImpl.getUsers(queryUser);
    }
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody User user) {
        return Result.<Boolean>builder().data(userServiceImpl.updateById(user)).build();
    }
    @PostMapping("/remove")
    public Result<Boolean> removeUser(@RequestBody User user) {
        return Result.<Boolean>builder().data(userServiceImpl.removeById(user)).build();
    }
    @GetMapping("/getWebsocketPage")
    public ResponseEntity<Void> login() {
        String internalUri = "/internal_protected/" + Util.encodePath("/websocketPage.html");
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Accel-Redirect", internalUri);

        String instanceId = "";
//        nacosRegistration.
//        String instanceId = registration.getInstanceId();
//        System.out.println(instanceId);
//        System.out.println("n"+ninstanceId);
        NamingService namingService = null;
        try {
            namingService = NamingFactory.createNamingService(env.getProperty("spring.cloud.nacos.discovery.server-addr") + ":8848");
            String instanceIP = env.getProperty("spring.cloud.nacos.discovery.ip");
            List<Instance> allInstances = namingService.getAllInstances(env.getProperty("spring.application.name"));
            for(Instance i:allInstances){
                if (i.getIp().equals(instanceIP)){
                    instanceId = i.getInstanceId();
                }
            }
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
        ResponseCookie cookie = ResponseCookie.from("sc-lb-itc-id", instanceId)
                .path("/")
                .maxAge(24 * 60 * 60)
                .httpOnly(true)
                // .secure(true)   // 如果需要Secure，取消注释
                .build();
        headers.add(HttpHeaders.SET_COOKIE,cookie.toString());

        return ResponseEntity.ok().headers(headers).build();
    }
}
