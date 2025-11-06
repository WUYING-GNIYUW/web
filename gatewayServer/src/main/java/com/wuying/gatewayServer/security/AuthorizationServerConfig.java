package com.wuying.gatewayServer.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@Configuration
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AuthorizationServerConfig {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(getCustomClientRegistration());
    }

    private ClientRegistration getCustomClientRegistration() {
        return ClientRegistration.withRegistrationId("gateway-client-registration")
                .clientId("gateway-client-id")
                .clientSecret("$2a$10$nDUzPNsp3WUWmSC3Mao3Q.hde98tpdUqh7MXLznpzbpOl/rEiD5C6")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost/login/oauth2/code/gatewayClient")
                .scope("openid")
                .authorizationUri("http://authorizationserver:8100")
                .clientName("gateway-client")
                .build();
    }

    // 配置 OAuth2 客户端2
//    private ClientRegistration getGithubClientRegistration() {
//        return ClientRegistration.withRegistrationId("github")
//                .clientId("your-github-client-id")  // 在 GitHub 中创建的 OAuth 应用的 Client ID
//                .clientSecret("your-github-client-secret")  // 在 GitHub 中创建的 OAuth 应用的 Client Secret
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .scope("user:email")  // GitHub 的 scope 可以是 user:email，或者其它你需要的权限
//                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")  // GitHub 回调的 URI
//                .authorizationUri("https://github.com/login/oauth/authorize")  // GitHub 授权 URI
//                .tokenUri("https://github.com/login/oauth/access_token")  // GitHub 获取 token 的 URI
//                .userInfoUri("https://api.github.com/user")  // 获取用户信息的 URI
//                .userNameAttributeName("login")  // GitHub API 返回的登录用户名的属性名
//                .clientName("GitHub")  // 客户端名称
//                .build();
//    }

}