package com.wuying.authorizationServer.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@Configuration
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AuthorizationServerConfig {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(getCustomClientRegistration(),getGitHubClientRegistration());
    }

    private ClientRegistration getCustomClientRegistration() {
        return ClientRegistration.withRegistrationId("gateway-client-registration")
                .clientId("gateway-client-id")
                .clientSecret("000000")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost/login/oauth2/code/gatewayClient")
                .scope("openid")
                .authorizationUri("http://authorizationserver:8100")
                .clientName("gateway-client")
                .build();
    }

    private ClientRegistration getGitHubClientRegistration() {
        return CommonOAuth2Provider.GITHUB
                .getBuilder("github")
                .clientId("Ov23lir1hPmC3gRHwFoX")
                .clientSecret("79e8bf24882a51c2785d6304ad1047ad59c81997")
                .scope("read:user", "user:email")
                .redirectUri("http://localhost/login/oauth2/code/gatewayClient")
                .build();
    }

}