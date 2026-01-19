package com.wuying.common.pojo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wuying.common.Marker.Create;
import com.wuying.common.Marker.Update;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {
    @TableId(type = IdType.AUTO)
    @NotBlank(groups = {Create.class, Update.class})
    private String userId;
    @NotBlank(groups = {Create.class})
    private String password;
    @NotBlank(groups = {Create.class})
    private String userName;
    private Boolean availableFlag;
    private Boolean onlineFlag;
    private String roles;
    private String origin;
}
