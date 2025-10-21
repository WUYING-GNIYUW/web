package com.wuying.userServer.eventListener;

import com.wuying.userServer.task.TaskExm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;


@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WebSocketConnectListener {

    private final TaskExm taskExm;
    @EventListener
    public void SockJSConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = sha.getUser();
        //String sessionId = sha.getSessionId();
        System.out.println("SockJS 会话已创建, user =" + principal.getName());
        taskExm.startTask(principal);
    }

    @EventListener
    public void SockJSDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = sha.getSessionId();
        System.out.println("SockJS 会话已销毁, sessionId=" + sessionId);
        taskExm.stopTask();
    }
}