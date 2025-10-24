package com.wuying.userServer.controller;

import com.wuying.common.pojo.Result;
import com.wuying.userServer.service.ConversationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
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
    @MessageMapping("/conversation/message/{userId}")
    public Result accpetMessage(@DestinationVariable String userId, @Payload String message) {
        return conversationServiceImpl.forwardMessage(userId, message);
    }
}