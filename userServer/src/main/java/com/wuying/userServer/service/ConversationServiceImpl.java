package com.wuying.userServer.service;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuying.common.pojo.Conversation;
import com.wuying.common.pojo.Result;
import com.wuying.common.util.Util;
import com.wuying.userServer.mapper.ConversationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation> implements ConversationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final Environment env;
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
        message = message.concat(",sender:local");
        messagingTemplate.convertAndSendToUser(userId,"/queue/messages",message);
        System.out.println(userId+"------"+message);
        return Result.builder().build();
    }


}
