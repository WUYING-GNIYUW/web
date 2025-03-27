package authorizationServer.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConstant {
    private String signing_key;
    private String token_key;
    private String thread_userid_key;
    private String thread_authority_key;
}
