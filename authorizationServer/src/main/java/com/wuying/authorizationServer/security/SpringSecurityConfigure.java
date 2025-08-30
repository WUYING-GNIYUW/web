package com.wuying.authorizationServer.security;



import cn.hutool.core.lang.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SpringSecurityConfigure {
    //final CustomUserInfoMapper customUserInfoMapper;
    private final DBUserDetailsManager dbUserDetailsManager;
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((authorize) ->
//                authorize
//                        .requestMatchers("/loginPage","/login/**").permitAll()
//                        .anyRequest().authenticated()
//
//        );
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer();

        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (authorizationServer) ->
                        authorizationServer.oidc(Customizer.withDefaults()
//                                        .userInfoEndpoint(userinfo ->
//                                                userinfo.userInfoMapper(customUserInfoMapper)
                                        //)
                        )
                ).authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("error/**","/favicon.ico").permitAll()
                                .anyRequest().authenticated()
                );
//               ).exceptionHandling(exceptions ->
//                       exceptions
//                               .defaultAuthenticationEntryPointFor(
//                                       new LoginUrlAuthenticationEntryPoint("/login"),
//                                       new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
//                               )
        //http.oauth2Login(Customizer.withDefaults());
        http.formLogin(form ->
                form
                        .loginPage("/getLoginPage")
                        .loginProcessingUrl("/login")
                        .usernameParameter("userId") // 使用我们自己的登录页
                        .permitAll()
        );
        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        http
                .securityMatcher("/**")
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/error/**","/favicon.ico","/getLoginPage","/hello").permitAll()
                                .anyRequest().authenticated()
                );
//        http.requestCache(Cache -> Cache.
//                requestCache(requestCache));
//        http.exceptionHandling(exception ->
//                exception
//                        //.authenticationEntryPoint(new MyAuthenticationEntryPoint())
//                        .accessDeniedHandler(new MyAccessDeniedHandler()));

                //.successHandler(new MyAuthenticationSuccessHandler())
                //.failureHandler(new MyAuthenticationFailureHandler())
//        http.formLogin(login ->
//                        login
//                                .loginPage("/login")
//                                .loginProcessingUrl("/logining")
//                                .permitAll()
//                                .usernameParameter("userId")
//                                .passwordParameter("password")
//                                //.successHandler(new MyAuthenticationSuccessHandler())
//                                //.failureHandler(new MyAuthenticationFailureHandler())
//        );
//
//        http.formLogin(Customizer.withDefaults());
        http.formLogin(form ->
                form
                        .loginPage("/getLoginPage")
                        .loginProcessingUrl("/login")// 使用我们自己的登录页
                        .usernameParameter("userId")
                        .permitAll()
        );
        http.userDetailsService(dbUserDetailsManager);
//        http.exceptionHandling(exception ->
//                exception
//                        //.authenticationEntryPoint(new MyAuthenticationEntryPoint())
//                        .accessDeniedHandler(new MyAccessDeniedHandler())
//);
//        http.logout(logout ->
//                logout.permitAll()
//                        .logoutSuccessHandler(new MyLogoutSuccessHandler())
//        );

//        );
        //http.addFilterBefore(new ParseJwtFilter(), UsernamePasswordAuthenticationFilter.class);
        //http.addFilterAfter(new SetJwtFilter(), UsernamePasswordAuthenticationFilter.class);

        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);


        return http.build();
    }

//    @Bean
//    public RegisteredClientRepository registeredClientRepository() {
//        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                .clientId("gatewayclient")
//                .clientSecret(passwordEncoder().encode("000000"))
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUri("http://www.localhost:8080/login/oauth2/code/gatewayClient")
//                .scope(OidcScopes.OPENID)
//                .scope(OidcScopes.PROFILE)
//                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
//                .build();
//        System.out.println(registeredClient.toString());
//        return new InMemoryRegisteredClientRepository(registeredClient);
//    }



    @Bean//重新配置防火墙过滤器，允许url带有特殊字符
    public HttpFirewall allowUrlSemicolonHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true);
        return firewall;
    }

}
