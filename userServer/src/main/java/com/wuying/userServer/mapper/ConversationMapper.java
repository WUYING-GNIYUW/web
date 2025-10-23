package com.wuying.userServer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuying.common.pojo.Conversation;
import com.wuying.common.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

@Mapper
@Transactional(rollbackFor = {Exception.class})
public interface ConversationMapper extends BaseMapper<Conversation> {
}
