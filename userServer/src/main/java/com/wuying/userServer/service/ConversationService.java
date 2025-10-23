package com.wuying.userServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuying.common.pojo.Conversation;
import com.wuying.common.pojo.Result;
import com.wuying.common.pojo.User;

public interface ConversationService extends IService<Conversation> {
    Result buildTemporaryConversations(String hostUserId, String ContactUserId);

    Result accpetMessage(String userId, String message);

    Result forwardMessage(String userId, String message);
}
