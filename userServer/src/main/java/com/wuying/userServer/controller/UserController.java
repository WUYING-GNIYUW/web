package com.wuying.userServer.controller;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.wuying.common.pojo.Result;
import com.wuying.common.pojo.User;
import com.wuying.common.pojo.UserInfo;
import com.wuying.common.util.Util;
import com.wuying.userServer.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Tag(name = "Foruser")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @Operation(summary = "普通user请求")
    @GetMapping("/test/{id}")
    public Result<User> getByIdJustForTest(@PathVariable("id") Long id) {
        User user = userServiceImpl.getById(id);

        return Result.<User>builder().data(user).build();
    }

    @GetMapping("/test")
    public String ForTest() {
        return "ok";
    }

    @PostMapping("/add")
    public Result<Boolean> addUser(@RequestBody User addedUser) {
        return userServiceImpl.addUser(addedUser);
    }

    @PostMapping("/register")
    public Result<Boolean> registerUser(@RequestBody User registeredUser) {
        return userServiceImpl.addUser(registeredUser);
    }

    @PostMapping("/get")
    public Result<List<User>> getUsers(@RequestBody User queriedUser) {
        return userServiceImpl.getUsers(queriedUser);
    }

    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody User UpdatedUser) {
        return Result.<Boolean>builder().data(userServiceImpl.updateById(UpdatedUser)).build();
    }

    @PostMapping("/remove")
    public Result<Boolean> removeUser(@RequestBody User remomvedUser) {
        return Result.<Boolean>builder().data(userServiceImpl.removeById(remomvedUser)).build();
    }

    @GetMapping("/getInstances")
    public Result<List<Instance>> getInstances() {
        return userServiceImpl.getInstances();
    }

    @PostMapping("/startSticky")
    public ResponseEntity<Void> startSticky(@RequestBody Instance chosenInstance, @AuthenticationPrincipal Jwt jwt, Principal principal) {
        return userServiceImpl.startSticky(chosenInstance,jwt,principal);
    }

    @GetMapping("/getUserInfo")
    public ResponseEntity<Result<UserInfo>> getUserInfo(@PathVariable String queriedUserId) {
        return userServiceImpl.getUserInfo(queriedUserId);
    }

    @GetMapping("/getUserInfos")
    public ResponseEntity<Result<List<Map<String, Object>>>> getUserInfos(Principal principal) {
        return userServiceImpl.getUserInfos(principal);
    }

}
