package gatewayServer.security;
import gatewayServer.security.exceptionHandler.CustomAccessDeniedHandler;
import gatewayServer.security.exceptionHandler.CustomAuthenticationEntryPoint;
import gatewayServer.security.loginHandler.CustomAuthenticationFailureHandler;
import gatewayServer.security.loginHandler.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;


@EnableWebFluxSecurity
@Configuration
public class SpringSecurityConfigure {
    //@Bean
    public SecurityWebFilterChain defaultSecurityFilterChain(ServerHttpSecurity http) {

        WebSessionServerRequestCache requestCache = new WebSessionServerRequestCache();
        // 禁用csrf与cors
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.cors(ServerHttpSecurity.CorsSpec::disable);

        // 开启全局验证
        http.authorizeExchange((authorize) -> authorize
                .pathMatchers(
                        "/api/**","error/**","/favicon.ico"
                ).permitAll()
                .anyExchange().authenticated()
        );
//        http.oauth2Login(oauth2Login -> oauth2Login
//                .authenticationSuccessHandler(new CustomAuthenticationSuccessHandler(requestCache))
//                .authenticationFailureHandler(new CustomAuthenticationFailureHandler())
//
//        ).requestCache(Cache -> Cache.
//                requestCache(requestCache));
        http.oauth2Login(Customizer.withDefaults()
        )
        ;
//                .requestCache(Cache -> Cache.
//                requestCache(requestCache));
        http.oauth2Client(Customizer.withDefaults()).requestCache(Cache ->Cache.requestCache(requestCache));

//        http.exceptionHandling(exception -> exception
//                //.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
//                .accessDeniedHandler(new CustomAccessDeniedHandler())// 未认证用户访问受保护资源.accessDeniedHandler(accessDeniedHandler)       // 已认证用户权限不足
//        );
        return http.build();
    }
//    @Bean
//    public ReactiveClientRegistrationRepository clientRegistrationRepository(
//            ClientRegistration MyClientRegistration
//    ) {
//        return new InMemoryReactiveClientRegistrationRepository(MyClientRegistration);
//    }
//
//    @Bean
//    public ClientRegistration MyClientRegistration(
//    ) {
//        return ClientRegistration.withRegistrationId("gatewayclient")
//                .clientId("gatewayclient")
//                .clientSecret("000000")
//                .scope("openid", "profile")
//                .issuerUri("http://authorizationserver:8081")
//                .authorizationUri("http://authorizationserver:8081/oauth2/authorize")
//                .tokenUri("http://authorizationserver:8081/oauth2/token")
//                .userInfoUri("http://authorizationserver:8081/userinfo")
//                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
//                .clientName("MyClient")
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .build();
//    }
}
