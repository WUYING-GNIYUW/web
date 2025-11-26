package com.wuying.userServer.service;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuying.common.pojo.Result;
import com.wuying.common.pojo.User;
import com.wuying.common.pojo.UserInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.security.Principal;
import java.util.List;
import java.util.Map;


public interface UserService extends IService<User> {
    Result<Boolean> addUser(User addUser);
    Result<List<User>> getUsers(User queryUser);
    Result<Boolean> removeUser(User user);
    ResponseEntity<Void> startSticky(Instance chosenInstance, Jwt jwt, Principal principal);
    Result<List<Instance>> getInstances();
    ResponseEntity<Result<UserInfo>> getUserInfo(String userId);
    ResponseEntity<Result<List<Map<String, Object>>>> getUserInfos(Principal principal);

}
