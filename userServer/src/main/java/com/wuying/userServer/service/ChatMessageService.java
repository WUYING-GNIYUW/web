package com.wuying.userServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuying.common.pojo.ChatMessage;
import com.wuying.common.pojo.Result;

import java.security.Principal;

public interface ChatMessageService extends IService<ChatMessage> {
    Result<?> buildTemporaryConversations(String hostUserId, String ContactUserId);

    Result<?> accpetMessage(String userId, String message);

    Result<?> forwardMessage(ChatMessage chatMessage, Principal principal);

}
