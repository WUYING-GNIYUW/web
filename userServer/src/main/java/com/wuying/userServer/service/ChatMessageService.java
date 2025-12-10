package com.wuying.userServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuying.common.pojo.ChatMessage;
import com.wuying.common.pojo.Result;

import java.security.Principal;
import java.time.Instant;
import java.util.List;

public interface ChatMessageService extends IService<ChatMessage> {
    Result<?> buildTemporaryConversations(String hostUserId, String ContactUserId);

    Result<?> accpetMessage(String userId, String message);

    Result<?> forwardMessage(ChatMessage chatMessage, Principal principal);

    List<ChatMessage> readHistoryMessage(String queriedUserId, Principal principal);

    List<ChatMessage> readUnReadMessage(String queriedUserId, Principal principal);

}
