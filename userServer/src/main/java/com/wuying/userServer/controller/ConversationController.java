package com.wuying.userServer.controller;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.wuying.common.pojo.Result;
import com.wuying.common.pojo.UserInfo;
import com.wuying.userServer.service.ConversationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/conversation")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@RequestMapping("/conversation")
public class ConversationController {
    private final ConversationServiceImpl conversationServiceImpl;

    @GetMapping("/info_of_conversations")
    public Result getInfoOfConversations(@RequestParam String contactUserId, @AuthenticationPrincipal Jwt jwt) {
        return conversationServiceImpl.buildTemporaryConversations(jwt.getClaimAsString("userId"),contactUserId);
    }
    @PostMapping("/build_temporary_conversations")
    public Result buildTemporaryConversations(@RequestParam String contactUserId, @AuthenticationPrincipal Jwt jwt) {
        return conversationServiceImpl.buildTemporaryConversations(jwt.getClaimAsString("userId"),contactUserId);
    }
    @MessageMapping("/user/conversation/message/{userId}")
    public Result forwardMessage(@DestinationVariable String userId, @Payload String message) {
        return conversationServiceImpl.forwardMessage(userId, message);
    }


}