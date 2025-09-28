package com.wuying.authorizationServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuying.common.pojo.Result;
import com.wuying.common.pojo.User;

import java.util.List;


public interface UserService extends IService<User> {
    Result<List<User>> getUsers(User queryUser);


}
