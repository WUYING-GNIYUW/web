package com.wuying.userServer.service;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuying.common.pojo.Result;
import com.wuying.common.pojo.User;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface UserService extends IService<User> {
    Result<Boolean> addUser(User addUser);
    Result<List<User>> getUsers(User queryUser);
    Result<Boolean> removeUser(User user);
    ResponseEntity<Void> getWebsocketPage();
    ResponseEntity<Void> startSticky(Instance chosenInstance);
}
