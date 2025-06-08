package com.wuying.userServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuying.userServer.pojo.Result;
import com.wuying.userServer.pojo.User;

import java.util.List;


public interface UserService extends IService<User> {
    Result<Boolean> addUser(User addUser);
    Result<List<User>> getUsers(User queryUser);
    Result<Boolean> removeUser(User user);

}
