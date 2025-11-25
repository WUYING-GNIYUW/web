package com.wuying.userServer.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuying.common.pojo.UserInfo;
import com.wuying.common.util.Util;
import com.wuying.userServer.exception.AddUserException;
import com.wuying.userServer.mapper.UserMapper;
import com.wuying.common.pojo.Result;
import com.wuying.common.pojo.User;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RKeys;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.options.KeysScanOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;


import java.security.Principal;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
//@Transactional(rollbackFor = {Exception.class})
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
    private final UserMapper userMapper;
    private final RedissonClient redissonClient;
    private final Environment env;
    @Override
    public Result<Boolean> addUser(User adduser) {
        if (getById(adduser.getUserId()) == null) {
            save(adduser);
            return Result.<Boolean>builder().data(true).build();
        } else {
            throw new AddUserException("账号已存在");
        }
    }
    @Override
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
    @Override
    public Result<Boolean> removeUser(User removeUser) {
/*        List<User> Users = new ArrayList<>();
        List<String> UserIds = new ArrayList<>();
        Users.forEach(User->{UserIds.add(User.getUsername());});
        Users.forEach(System.out::println);*/
        if (getById(removeUser.getUserId()) != null) {
            removeById(removeUser);
            return Result.<Boolean>builder().data(true).build();
        } else {
            throw new AddUserException("账号不存在");
        }
    }

    @Override
    public Result<List<Instance>> getInstances(){
        try {
            NamingService namingService = Util.getNamingService(env);
            String instanceIP = env.getProperty("spring.cloud.nacos.discovery.ip");
            List<Instance> allInstances = namingService.getAllInstances(env.getProperty("spring.application.name"));
            allInstances.stream().forEach(i->System.out.println(i.toString()));
            return Result.<List<Instance>>builder().data(allInstances).build();
        }
        catch (NacosException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public ResponseEntity<Void> startSticky(Instance chosenInstance, Jwt jwt, Principal principal) {
        try {
            HttpHeaders headers = new HttpHeaders();
            NamingService namingService = Util.getNamingService(env);
            List<Instance> allInstances = namingService.getAllInstances(env.getProperty("spring.application.name"));
            Optional<Instance> firstInstance = allInstances.stream().filter(i -> i.getIp().equals(chosenInstance.getIp())).findFirst();
            Instance instance = firstInstance.orElseThrow(RuntimeException::new);
            ResponseCookie cookie = instance.getInstanceId() != null ? ResponseCookie.from("sc-lb-itc-id", instance.getInstanceId())
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .httpOnly(true)
                    // .secure(true)   // 如果需要Secure，取消注释
                    .build() : null;
            if (cookie != null) {
                headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            }

            RMap<String, Object> userMap = redissonClient.getMap("user:" + jwt.getSubject());
            userMap.put("instanceId",instance.getInstanceId());
            userMap.put("ip",instance.getIp());
            userMap.put("port",instance.getPort());
            userMap.put("serviceName",instance.getServiceName());
            userMap.expire(Duration.ofHours(12));
            return ResponseEntity.ok().headers(headers).build();
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Result<UserInfo>> getUserInfo(String userId) {
        RMap<String, Object> userMap = redissonClient.getMap("userinfo:" + userId);
        if(userMap.isExists()){
            UserInfo userInfo = UserInfo.builder().build();
            BeanUtil.fillBeanWithMapIgnoreCase(userMap.readAllMap(), userInfo, false);
            return ResponseEntity.ok(Result.<UserInfo>builder().data(userInfo).build());
        }
        else{
            return ResponseEntity.ok(Result.<UserInfo>builder().message("userInfo unexists").build());
        }

    }
    @Override
    public ResponseEntity<Result<List<Map<String, Object>>>> getUserInfos() {

        RKeys rKeys = redissonClient.getKeys();

        KeysScanOptions options = KeysScanOptions.defaults()
                .pattern("user:*")
                .limit(300);
        List<Map<String, Object>> userInfoList = rKeys.getKeysStream(options)
                .map(key -> {
                    RMap<String, Object> userMap = redissonClient.getMap(key);
                    if(userMap.isExists()){
                        Map<String, Object> userInfoMap = userMap.readAllMap();
                        userMap.put("userId", key.replaceFirst("user:", ""));
                        return userInfoMap;
                    }
                    else{
                        return new HashMap<String, Object>();
                    }
                })
                .toList();
        return ResponseEntity
                .ok(Result
                        .<List<Map<String, Object>>>builder()
                        .data(userInfoList)
                        .build());
    }
}
