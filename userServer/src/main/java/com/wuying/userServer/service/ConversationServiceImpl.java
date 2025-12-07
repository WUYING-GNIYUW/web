package com.wuying.userServer.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuying.common.pojo.ChatMessage;
import com.wuying.common.pojo.Result;
import com.wuying.common.pojo.User;
import com.wuying.userServer.mapper.ChatMessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import cn.hutool.json.JSONUtil;
import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        messagingTemplate.convertAndSendToUser(chatMessage.getReceivedUserId(),"/queue/messages",JSONUtil.toJsonStr(chatMessage));
        System.out.println(chatMessage.getReceivedUserId());
        return Result.<Boolean>builder().data(StorageMessage(chatMessage)).build();

    }
    @Override
    public Result<List<ChatMessage>> readHistoryMessage(String queriedUserId, Principal principal) {
        String customClientUserId = principal.getName();
        List<ChatMessage> historyMessageListList = lambdaQuery()
                .and(w -> w.eq(ChatMessage::getSendUserId, customClientUserId)
                        .eq(ChatMessage::getReceivedUserId, queriedUserId))
                .or(w -> w.eq(ChatMessage::getSendUserId, queriedUserId)
                        .eq(ChatMessage::getReceivedUserId, customClientUserId))
                .orderByAsc(ChatMessage::getCreatedTime)
                .list();

//        List<ChatMessage> historyMessageListList = Stream
//                .concat(
//                        lambdaQuery()
//                                .eq(queriedUserId != null, ChatMessage::getSendUserId, queriedUserId)
//                                .eq(customClientUserId != null, ChatMessage::getReceivedUserId, customClientUserId)
//                                .list().stream(),
//                        lambdaQuery()
//                                .eq(customClientUserId != null, ChatMessage::getSendUserId, customClientUserId)
//                                .eq(queriedUserId != null, ChatMessage::getReceivedUserId, queriedUserId)
//                                .list().stream()
//                ).toList();
     return Result.<List<ChatMessage>>builder().data(historyMessageListList).build();
    }



    private Boolean StorageMessage(ChatMessage chatMessage) {
        return save(chatMessage);
    }
}
