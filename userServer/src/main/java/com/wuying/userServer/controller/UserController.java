package com.wuying.userServer.controller;

import com.wuying.common.Marker.Create;
import com.wuying.common.Marker.Update;
import com.wuying.common.pojo.Result;
import com.wuying.common.pojo.User;
import com.wuying.common.pojo.UserInfo;
import com.wuying.userServer.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user")
@Tag(name = "ForUser")
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
    public Result<Boolean> addUser(@RequestBody @Validated(Create.class) User addedUser) {
        return Result.<Boolean>builder().data(userServiceImpl.addUser(addedUser)).build();
    }

    @PostMapping("/register")
    public Result<Boolean> registerUser(@RequestBody @Validated(Create.class) User registeredUser) {
        return Result.<Boolean>builder().data(userServiceImpl.addUser(registeredUser)).build();
    }

    @PostMapping("/get")
    public Result<List<User>> getUsers(@RequestBody User queriedUser) {
        return Result.<List<User>>builder().data(userServiceImpl.getUsers(queriedUser)).build();
    }

    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody @Validated(Update.class) User UpdatedUser) {
        return Result.<Boolean>builder().data(userServiceImpl.updateById(UpdatedUser)).build();
    }

    @PostMapping("/remove")
    public Result<Boolean> removeUser(@RequestBody @Validated(Update.class) User remomvedUser) {
        return Result.<Boolean>builder().data(userServiceImpl.removeById(remomvedUser)).build();
    }

    @GetMapping("/identifySelfClient")
    public Result<User> identifySelfClient(Principal principal) {
        return Result.<User>builder().data(userServiceImpl.getById(principal.getName())).build();
    }

    @GetMapping("/getFriends")
    public Result<Map<String,List<User>>> getFriends(Principal principal) {
        return Result.<Map<String,List<User>>>builder().data(userServiceImpl.getFriends(principal)).build();
    }

//    @PostMapping("/startSticky")
//    public ResponseEntity<Void> startSticky(@RequestBody Instance chosenInstance, @AuthenticationPrincipal Jwt jwt, Principal principal) {
//        return ResponseEntity.ok().headers(userServiceImpl.startSticky(chosenInstance,jwt,principal)).build();
//    }

    @GetMapping("/getUserInfo")
    public Result<UserInfo> getUserInfo(@PathVariable String queriedUserId) {
        return Result.<UserInfo>builder().data(userServiceImpl.getUserInfo(queriedUserId)).build();
    }

//    @GetMapping("/getUserInfos")
//    public Result<List<Map<String, Object>>> getUserInfos(Principal principal) {
//        return Result.<List<Map<String, Object>>>builder().data(userServiceImpl.getUserInfos(principal)).build();
//    }


}
