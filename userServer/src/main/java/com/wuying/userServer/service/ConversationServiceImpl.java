package com.wuying.userServer.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuying.common.pojo.ChatMessage;
import com.wuying.common.pojo.Result;
import com.wuying.common.pojo.UserInfo;
import com.wuying.userServer.mapper.ChatMessageMapper;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.stream.StreamAddArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import cn.hutool.json.JSONUtil;
import java.security.Principal;
import java.time.Instant;
import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ConversationServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {
    private final SimpMessagingTemplate messagingTemplate;
    private final RedissonClient redisson;
    private final UserServiceImpl userServiceImpl;
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
    public Boolean forwardMessage(ChatMessage chatMessage, Principal principal) {
        chatMessage.setCreatedTime(Instant.now());
        chatMessage.setSendUserId(principal.getName());
//        messagingTemplate.convertAndSendToUser(chatMessage.getReceivedUserId(),"/queue/messages",JSONUtil.toJsonStr(chatMessage));
        UserInfo userInfo = userServiceImpl.getUserInfo(chatMessage.getReceivedUserId());
        if(userInfo != null){
            String userSocketServerIp = userInfo.getSocketServerIp();
            if(userSocketServerIp.equals(env.getProperty("spring.cloud.nacos.discovery.ip"))){
                messagingTemplate.convertAndSendToUser(chatMessage.getReceivedUserId(),"/queue/messages",JSONUtil.toJsonStr(chatMessage));
            }
            else{
                RStream<String, ChatMessage> stream = redisson.getStream("chat-stream:".concat(userSocketServerIp));
                StreamAddArgs<String, ChatMessage> args = StreamAddArgs.entry("*", chatMessage);
                stream.add(args);
            }
            chatMessage.setUnreadFlag(false);
        }
        else{
            chatMessage.setUnreadFlag(true);
        }
        return storageMessage(chatMessage);
    }
    @Override
    public List<ChatMessage> readHistoryMessage(String queriedUserId, Principal principal) {
        String customClientUserId = principal.getName();

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
     return lambdaQuery()
             .and(w -> w
                     .eq(ChatMessage::getSendUserId, customClientUserId)
                     .eq(ChatMessage::getReceivedUserId, queriedUserId))
             .or(w -> w
                     .eq(ChatMessage::getSendUserId, queriedUserId)
                     .eq(ChatMessage::getReceivedUserId, customClientUserId)
                     .eq(ChatMessage::getUnreadFlag, false))
             .orderByAsc(ChatMessage::getCreatedTime) .list();
    }

    @Override
    public List<ChatMessage> readUnReadMessage(String queriedUserId, Principal principal) {
        String customClientUserId = principal.getName();
        List<ChatMessage> UnreadMessageList = lambdaQuery()
                .and(w -> w
                        .eq(ChatMessage::getSendUserId, queriedUserId)
                        .eq(ChatMessage::getReceivedUserId, customClientUserId)
                        .eq(ChatMessage::getUnreadFlag, true))
                .orderByAsc(ChatMessage::getCreatedTime)
                .list();

        lambdaUpdate()
                .and(w -> w
                        .eq(ChatMessage::getSendUserId, queriedUserId)
                        .eq(ChatMessage::getReceivedUserId, customClientUserId)
                        .eq(ChatMessage::getUnreadFlag, true))
                .set(ChatMessage::getUnreadFlag, false)
                .update();
        return UnreadMessageList;
    }




    private Boolean storageMessage(ChatMessage chatMessage) {
        return save(chatMessage);
    }
}
