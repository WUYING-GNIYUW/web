package com.wuying.authorizationServer.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AuthorizationServerConfig {

    private final Environment env;

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://106.53.106.123:8100")
                .build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        return new InMemoryRegisteredClientRepository(getCustomRegisteredClient());
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(getGitHubClientRegistration());
    }

    private RegisteredClient getCustomRegisteredClient() {
        return RegisteredClient.withId(env.getProperty("gateway-client.registration-client-id"))
                .clientName(env.getProperty("gateway-client.client-name"))
                .clientId(env.getProperty("gateway-client.client-id"))
                .clientSecret(env.getProperty("gateway-client.client-secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(env.getProperty("gateway-client.redirect-uri"))
                .scope(OidcScopes.OPENID)
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(3))
                        .refreshTokenTimeToLive(Duration.ofDays(3))
                        // 是否启用 refresh token 旋转（可选）
                        .reuseRefreshTokens(false)
                        .build())
                .build();
    }

    private ClientRegistration getGitHubClientRegistration() {
        return CommonOAuth2Provider.GITHUB
                .getBuilder("github")
                .clientId("Ov23lir1hPmC3gRHwFoX")
                .clientSecret("517b714798ed0ac9be395eb5d06bcfca49691b14")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://authorizationserver:8100/login/oauth2/code/github")
                .scope("read:user", "user:email")
                .build();
    }

}