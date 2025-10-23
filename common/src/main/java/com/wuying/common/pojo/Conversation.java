package com.wuying.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Conversation {
    @TableId(type = IdType.AUTO)
    private Long conversationId;
    private String AUserId;
    private String BUserId;
}
