package com.wuying.userServer.webSocket;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
//@EnableWebSocketSecurity
@EnableWebSocketMessageBroker
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class WebSocketConfigure implements WebSocketMessageBrokerConfigurer{

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");//广播前缀，单点前缀
        registry.setUserDestinationPrefix("/toUser");
        registry.setApplicationDestinationPrefixes("/toBack");//客户端向后端前缀
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/user/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}


