package com.wuying.userServer.pojo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Long userId;
    private String password;
    private String userName;
    private String availableState;
    private String onlineState;
    private String roles;
    private String origin;
}
