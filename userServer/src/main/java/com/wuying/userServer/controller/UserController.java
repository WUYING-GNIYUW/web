package com.wuying.userServer.controller;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.wuying.common.pojo.Result;
import com.wuying.common.pojo.User;
import com.wuying.common.util.Util;
import com.wuying.userServer.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Tag(name = "Foruser")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {
    private final UserServiceImpl userServiceImpl;

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
    public ResponseEntity<Void> getWebsocketPage() {
        return userServiceImpl.getWebsocketPage();
    }

    @PostMapping("/startSticky")
    public ResponseEntity<Void> startSticky(@RequestBody Instance chosenInstance) {
        return userServiceImpl.startSticky(chosenInstance);
    }
}
