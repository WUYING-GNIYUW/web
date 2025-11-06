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
                .clientId("Ov23liuoVd504lJJEM9H")
                .clientSecret("8ad0852ebaa229302c121952c7c40e6901f0639c")
                .scope("read:user", "user:email")
                .redirectUri("http://106.53.106.123/login/oauth2/code/github")
                .build();
    }

}