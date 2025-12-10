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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RKeys;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.options.KeysScanOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;


import java.security.Principal;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
//@Transactional(rollbackFor = {Exception.class})
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
    private final UserMapper userMapper;
    private final HttpServletResponse response;
    private final RedissonClient redissonClient;
    private final Environment env;
    @Override
    public Boolean addUser(User addeduser) {
        if (getById(addeduser.getUserId()) == null) {
            save(addeduser);
            return true;
        } else {
            throw new AddUserException("账号已存在");
        }
    }
    @Override
    public List<User> getUsers(User queriedUser) {
        List<User> userList = lambdaQuery()
                .eq(queriedUser.getUserId() != null, User::getUserId, queriedUser.getUserId())
                .like(queriedUser.getUserName() != null, User::getUserName, queriedUser.getUserName())
                .eq(queriedUser.getAvailableFlag() != null, User::getAvailableFlag, queriedUser.getAvailableFlag())
                .eq(queriedUser.getOnlineFlag() != null, User::getOnlineFlag, queriedUser.getOnlineFlag())
                .list();
//        Map<String, Object> mapOfUser = BeanUtil.beanToMap(queryUser);
//        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
//        list(userQueryWrapper.allEq(mapOfUser));
        return userList;
    }
    @Override
    public Boolean removeUser(User removedUser) {
        if (getById(removedUser.getUserId()) != null) {
            removeById(removedUser);
            return true;
        } else {
            throw new AddUserException("账号不存在");
        }
    }

//    @Override
//    public String identifySelfClient(Principal principal){
//        return Result.<String>builder().data(principal.getName()).build();
//    }

    @Override
    public Map<String,List<User>> getFriends(){
        List<User> userList = list();
        Map<Boolean, List<User>> grouped = userList.stream()
                .collect(Collectors.partitioningBy(User::getOnlineFlag));

        List<User> onlineUserList = grouped.get(true);
        List<User> offlineUserList = grouped.get(false);
        Map<String, List<User>> userMap = new HashMap<>();
        userMap.put("onlineUser",onlineUserList);
        userMap.put("offlineUser",offlineUserList);
        return userMap;

    }

//
//    }
//    @Override
//    public HttpHeaders startSticky(Instance chosenInstance, Jwt jwt, Principal principal) {
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            NamingService namingService = Util.getNamingService(env);
//            List<Instance> allInstances = namingService.getAllInstances(env.getProperty("spring.application.name"));
//            Optional<Instance> firstInstance = allInstances.stream().filter(i -> i.getIp().equals(chosenInstance.getIp())).findFirst();
//            Instance instance = firstInstance.orElseThrow(RuntimeException::new);
//
//            Cookie cookie = new Cookie("sc-lb-itc-id", "Error");
//            if (instance.getInstanceId() != null) {
//                cookie = new Cookie("sc-lb-itc-id", instance.getInstanceId());
//                cookie.setPath("/");
//                cookie.setMaxAge(24 * 60 * 60);
//                cookie.setHttpOnly(true);
//                response.addCookie(cookie);
//            } else {
//                System.out.println("Error");
//            }
//            response.addCookie(cookie);
//
//            RMap<String, Object> userMap = redissonClient.getMap("userinfo:".concat(principal.getName()));
//            userMap.put("instanceId", instance.getInstanceId());
//            userMap.put("ip", instance.getIp());
//            userMap.put("port", instance.getPort());
//            userMap.put("serviceName", instance.getServiceName());
//            userMap.expire(Duration.ofHours(12));
//            return headers;
//        } catch (NacosException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public UserInfo getUserInfo(String userId) {
        RMap<String, Object> userMap = redissonClient.getMap("userinfo:".concat(userId));
        if(userMap.isExists()){
            UserInfo userInfo = UserInfo.builder().build();
            BeanUtil.fillBeanWithMapIgnoreCase(userMap.readAllMap(), userInfo, false);
            return userInfo;
        }
        else{
            return null;
        }

    }
//    @Override
//    public List<Map<String, Object>> getUserInfos(Principal principal) {
//
//        RKeys rKeys = redissonClient.getKeys();
//
//        KeysScanOptions options = KeysScanOptions.defaults()
//                .pattern("userinfo:*")
//                .limit(300);
//        String ClientUserKey = "userinfo:".concat(principal.getName());
//        return rKeys.getKeysStream(options)
//                .filter(key -> !key.equals(ClientUserKey))
//                .map(key -> {
//                    RMap<String, Object> RUserInfoMap = redissonClient.getMap(key);
//                    if(RUserInfoMap.isExists()){
//                        //RUserInfoMap.put("userId", key.replaceFirst("user:", ""));
//                        return RUserInfoMap.readAllMap();
//                    }
//                    else{
//                        return null;
//                    }
//                })
//                .toList();
//    }
}
