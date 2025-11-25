package com.wuying.userServer.service;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuying.common.pojo.Conversation;
import com.wuying.common.pojo.Result;

import java.util.List;

public interface ConversationService extends IService<Conversation> {
    Result buildTemporaryConversations(String hostUserId, String ContactUserId);

    Result accpetMessage(String userId, String message);

    Result forwardMessage(String userId, String message);

}
