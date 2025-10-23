package com.wuying.userServer.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuying.common.pojo.Conversation;
import com.wuying.common.pojo.Result;
import com.wuying.common.pojo.User;
import com.wuying.userServer.exception.AddUserException;
import com.wuying.userServer.mapper.ConversationMapper;
import com.wuying.userServer.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation> implements ConversationService {
    private final SimpMessagingTemplate messagingTemplate;
    @Override
    public Result buildTemporaryConversations(String hostUserId, String ContactUserId) {
        save(Conversation.builder().build());
        return Result.<Boolean>builder().data(true).build();
    }

    @Override
    public Result accpetMessage(String userId, String message) {
        return null;
    }

    @Override
    public Result forwardMessage(String userId, String message) {
        messagingTemplate.convertAndSendToUser(userId,"/queue/messages",message);
        return Result.builder().build();
    }

}
