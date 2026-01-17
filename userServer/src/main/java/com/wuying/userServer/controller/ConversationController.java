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
    @MessageMapping("/user/conversation/message")
    public Result<Boolean> forwardMessage(@Payload ChatMessage chatMessage, Principal principal) {
        return Result.<Boolean>builder().data(conversationServiceImpl.forwardMessage(chatMessage, principal)).build();
    }
    @GetMapping("/readHistoryMessage")
    public Result<List<ChatMessage>> readHistoryMessage(@RequestParam String queriedUserId, Principal principal) {
        return Result.<List<ChatMessage>>builder().data(conversationServiceImpl.readHistoryMessage(queriedUserId, principal)).build();
    }
    @GetMapping("/readUnreadMessage")
    public Result<List<ChatMessage>> readUnReadMessage(@RequestParam String queriedUserId, Principal principal) {
        return Result.<List<ChatMessage>>builder().data(conversationServiceImpl.readUnReadMessage(queriedUserId, principal)).build();
    }



}