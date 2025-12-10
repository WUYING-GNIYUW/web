package com.wuying.userServer.listener;

import cn.hutool.json.JSONUtil;
import com.wuying.common.pojo.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ChatMessageListener implements StreamListener<String, ObjectRecord<String, ChatMessage>> {
    private final SimpMessagingTemplate messagingTemplate;
    @Override
    public void onMessage(ObjectRecord<String, ChatMessage> record) {
        ChatMessage chatMessage = record.getValue();
        messagingTemplate.convertAndSendToUser(chatMessage.getReceivedUserId(),"/queue/messages", JSONUtil.toJsonStr(chatMessage));
    }
}
