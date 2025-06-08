package com.wuying.common.pojo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private String userId;
    private String password;
    private String username;
    private Integer availableState;
    private Integer onlineState;
    private String roles;
    private String origin;
}
