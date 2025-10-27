package com.wuying.userServer.service;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuying.common.util.Util;
import com.wuying.userServer.exception.AddUserException;
import com.wuying.userServer.mapper.UserMapper;
import com.wuying.common.pojo.Result;
import com.wuying.common.pojo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
//@Transactional(rollbackFor = {Exception.class})
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
    private final UserMapper userMapper;
    private final Environment env;

    public Result<Boolean> addUser(User adduser) {
        if(getById(adduser.getUserId())==null){
            save(adduser);
            return Result.<Boolean>builder().data(true).build();
        }
        else {
            throw new AddUserException("账号已存在");
        }
    }

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

    public Result<Boolean> removeUser(User removeUser) {
/*        List<User> Users = new ArrayList<>();
        List<String> UserIds = new ArrayList<>();
        Users.forEach(User->{UserIds.add(User.getUsername());});
        Users.forEach(System.out::println);*/
        if(getById(removeUser.getUserId()) != null){
            removeById(removeUser);
            return Result.<Boolean>builder().data(true).build();
        }
        else {
            throw new AddUserException("账号不存在");
        }
    }

    public ResponseEntity<Void> getWebsocketPage() {
        String internalUri = "/internal_protected/" + Util.encodePath("/websocketPage.html");
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Accel-Redirect", internalUri);

        return ResponseEntity.ok().headers(headers).build();
    }


    public ResponseEntity<Void> startSticky() {
        try {
            HttpHeaders headers = new HttpHeaders();
            NamingService namingService = Util.getNamingService(env);
            String instanceIP = env.getProperty("spring.cloud.nacos.discovery.ip");
            List<Instance> allInstances = namingService.getAllInstances(env.getProperty("spring.application.name"));
            Optional<Instance> firstInstance = allInstances.stream().filter(i -> i.getIp().equals(instanceIP)).findFirst();
            String instanceId = firstInstance.orElseThrow(RuntimeException::new).getInstanceId();
            ResponseCookie cookie = instanceId != null ? ResponseCookie.from("sc-lb-itc-id", instanceId)
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .httpOnly(true)
                    // .secure(true)   // 如果需要Secure，取消注释
                    .build() : null;
            if (cookie != null) {
                headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            }
            return ResponseEntity.ok().headers(headers).build();
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }
}
