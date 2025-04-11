package authorizationServer.security;

import common.feign.clients.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import common.pojo.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CustomUserInfoMapper implements Function<OidcUserInfoAuthenticationContext, OidcUserInfo> {
    private final UserClient userClient;
    @Override
    public OidcUserInfo apply(OidcUserInfoAuthenticationContext context) {
        // 从上下文获取用户标识
        String userId = context.getAuthorization().getPrincipalName();
        //List<User> users = userClient.getUsers(User.builder().userId(userId).build())
        //return users.get(0);
        return OidcUserInfo.builder().build();
    }
}