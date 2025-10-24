package com.wuying.userServer.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuying.common.pojo.Conversation;
import com.wuying.common.pojo.Result;
import com.wuying.userServer.mapper.ConversationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

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
        System.out.println(userId+"------"+message);
        return Result.builder().build();
    }

}
