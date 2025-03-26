package user.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "userddd")
@Data
public class UserConstant {
    public Integer available;
    public Integer unavailable;
    public Integer online;
    public Integer offline;

}
