package com.wuying.authorizationServer.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuying.authorizationServer.mapper.UserMapper;
import com.wuying.common.pojo.Result;
import com.wuying.common.pojo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@Transactional(rollbackFor = {Exception.class})
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {


    public Result<List<User>> getUsers(User queryUser) {
        List<User> userList = lambdaQuery()
                .eq(queryUser.getUserId() != null, User::getUserId, queryUser.getUserId())
                .like(queryUser.getUserName() != null, User::getUserName, queryUser.getUserName())
                .eq(queryUser.getAvailableState() != null, User::getAvailableState, queryUser.getAvailableState())
                .eq(queryUser.getOnlineState() != null, User::getOnlineState, queryUser.getOnlineState())
                .list();
//        Map<String, Object> mapOfUser = BeanUtil.beanToMap(queryUser);
//        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
//        list(userQueryWrapper.allEq(mapOfUser));
        return Result.<List<User>>builder().data(userList).build();
    }
}
