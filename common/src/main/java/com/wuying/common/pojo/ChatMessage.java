package com.wuying.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@Builder
@Data
public class ChatMessage {
    private String sendUserId;
    private String receivedUserId;
    private Instant createdTime;
    private String message;
}
