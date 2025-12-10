package com.wuying.userServer.controller;

import com.wuying.common.pojo.ChatMessage;
import com.wuying.common.pojo.Result;
import com.wuying.userServer.service.ConversationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/user/conversation")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@RequestMapping("/conversation")
public class ConversationController {
    private final ConversationServiceImpl conversationServiceImpl;

    @GetMapping("/info_of_conversations")
    public Result<?> getInfoOfConversations(@RequestParam String contactUserId, @AuthenticationPrincipal Jwt jwt) {
        return conversationServiceImpl.buildTemporaryConversations(jwt.getClaimAsString("userId"),contactUserId);
    }
    @PostMapping("/build_temporary_conversations")
    public Result<?> buildTemporaryConversations(@RequestParam String contactUserId, @AuthenticationPrincipal Jwt jwt) {
        return conversationServiceImpl.buildTemporaryConversations(jwt.getClaimAsString("userId"),contactUserId);
    }
    @MessageMapping("/user/conversation/message")
    public Result<Boolean> forwardMessage(@Payload ChatMessage chatMessage, Principal principal) {
        return conversationServiceImpl.forwardMessage(chatMessage, principal);
    }
    @GetMapping("/readHistoryMessage")
    public Result<List<ChatMessage>> readHistoryMessage(@RequestParam String queriedUserId, Principal principal) {
        return conversationServiceImpl.readHistoryMessage(queriedUserId, principal);
    }
    @GetMapping("/readUnReadMessage")
    public Result<List<ChatMessage>> readUnReadMessage(@RequestParam String queriedUserId, Principal principal) {
        return Result.<List<ChatMessage>>builder().data(conversationServiceImpl.readUnReadMessage(queriedUserId, principal)).build();
    }



}