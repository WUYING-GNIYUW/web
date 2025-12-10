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
    private String userName;
    private Boolean availableFlag;
    private Boolean onlineFlag;
    private String roles;
    private String origin;
    private String SocketServerIp;
}

