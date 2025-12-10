package com.wuying.userServer.listener;

import com.wuying.userServer.task.TaskExm;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;


@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WebSocketConnectListener {
    private final RedissonClient redissonClient;
    private final TaskExm taskExm;
    private final Environment env;
    @EventListener
    public void SockJSConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = sha.getSessionId();
        Principal principal = sha.getUser();
        if (principal != null) {
            RMap<Object, Object> userInfoMap = redissonClient.getMap("userInfo:".concat(principal.getName()));
            userInfoMap.put("stickyServerIp", env.getProperty("spring.cloud.nacos.discovery.ip"));
        }
        System.out.println("SockJS 会话已创建, sessionId=" + sessionId + " on " + env.getProperty("spring.cloud.nacos.discovery.ip"));
//        taskExm.startTask(principal);
    }

    @EventListener
    public void SockJSDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = sha.getSessionId();
        Principal principal = sha.getUser();
        System.out.println("SockJS 会话已销毁, sessionId=" + sessionId);
        if (principal != null) {
            RMap<Object, Object> map = redissonClient.getMap("userInfo:".concat(principal.getName()));
            map.delete();
        }

        taskExm.stopTask();
    }
}