package com.wuying.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserInfo {
    @TableId(type = IdType.AUTO)
    private String userId;
    @Builder.Default
    private String userName = "";
    @Builder.Default
    private Boolean availableFlag = true;
    @Builder.Default
    private Boolean onlineFlag = true;
    @Builder.Default
    private String roles = "";
    @Builder.Default
    private String origin = "";
    @Builder.Default
    private String SocketServerIp = "";
}

