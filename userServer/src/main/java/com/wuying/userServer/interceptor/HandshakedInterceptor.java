//package com.wuying.userServer.interceptor;
//
//import cn.hutool.core.bean.BeanUtil;
//import com.alibaba.nacos.api.naming.NamingService;
//import com.alibaba.nacos.api.naming.pojo.Instance;
//import com.wuying.common.pojo.UserInfo;
//import com.wuying.common.util.Util;
//import lombok.RequiredArgsConstructor;
//import org.redisson.api.RMap;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseCookie;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.HandshakeInterceptor;
//
//import java.time.Duration;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@Component
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//public class HandshakedInterceptor implements HandshakeInterceptor {
//
//    private final RedissonClient redissonClient;
//    private final Environment env;
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request,
//                                   ServerHttpResponse response,
//                                   WebSocketHandler wsHandler,
//                                   Map<String, Object> attributes) throws Exception {
//
//        String InstanceIp = env.getProperty("spring.cloud.nacos.discovery.ip");
//        NamingService namingService = Util.getNamingService(env);
//        List<Instance> allInstances = namingService.getAllInstances(env.getProperty("spring.application.name"));
//        Optional<Instance> firstInstance = allInstances.stream().filter(i -> i.getIp().equals(InstanceIp)).findFirst();
//        String instanceId = firstInstance.orElseThrow(RuntimeException::new).getInstanceId();
//
//        RMap<String, Object> userMap = redissonClient.getMap("userInfo:" + userId);
//        if(userMap.isExists()){
//            UserInfo userInfo = UserInfo.builder().build();
//            BeanUtil.fillBeanWithMapIgnoreCase(userMap.readAllMap(), userInfo, false);
//            return userInfo;
//        }
//
//
//        return true;
//    }
//
//    @Override
//    public void afterHandshake(ServerHttpRequest request,
//                               ServerHttpResponse response,
//                               WebSocketHandler wsHandler,
//                               Exception exception) {
//    }
//
//
//}