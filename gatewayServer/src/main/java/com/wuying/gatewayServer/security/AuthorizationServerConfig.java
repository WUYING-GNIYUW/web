package com.wuying.gatewayServer.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.registration.*;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.OidcScopes;

@Configuration
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AuthorizationServerConfig {
    private final Environment env;

    @Bean
    public ReactiveClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryReactiveClientRegistrationRepository(getCustomClientRegistration());
    }

    private ClientRegistration getCustomClientRegistration() {
        return ClientRegistrations.fromIssuerLocation(env.getProperty("gateway-client.issuer"))
                .registrationId(env.getProperty("gateway-client.registration-client-id"))
                .clientName(env.getProperty("gateway-client.client-name"))
                .clientId(env.getProperty("gateway-client.client-id"))
                .clientSecret(env.getProperty("gateway-client.client-secret"))
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(env.getProperty("gateway-client.redirect-uri"))
                .scope(OidcScopes.OPENID)
                .build();
    }

}