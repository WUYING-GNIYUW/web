package security.constant;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
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
