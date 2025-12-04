package com.wuying.userServer.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuying.common.pojo.ChatMessage;
import com.wuying.common.pojo.Result;
import com.wuying.userServer.mapper.ChatMessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ConversationServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {
    private final SimpMessagingTemplate messagingTemplate;
    private final Environment env;
    @Override
    public Result<?> buildTemporaryConversations(String hostUserId, String ContactUserId) {
        save(ChatMessage.builder().build());
        return Result.<Boolean>builder().data(true).build();
    }

    @Override
    public Result<?> accpetMessage(String userId, String message) {
        return null;
    }

    @Override
    public Result<Boolean> forwardMessage(ChatMessage chatMessage, Principal principal) {
        chatMessage.setCreatedTime(Instant.now());
        chatMessage.setSendUserId(principal.getName());
        messagingTemplate.convertAndSendToUser(chatMessage.getReceivedUserId(),"/queue/messages",chatMessage.toString());
        return Result.<Boolean>builder().data(StorageMessage(chatMessage)).build();

    }

    private Boolean StorageMessage(ChatMessage chatMessage) {
        return save(chatMessage);
    }
}
