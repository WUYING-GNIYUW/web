package authorizationServer.security;



import authorizationServer.security.handler.exceptionHandler.MyAccessDeniedHandler;
import authorizationServer.security.handler.loginHandler.MyAuthenticationFailureHandler;
import authorizationServer.security.handler.loginHandler.MyAuthenticationSuccessHandler;
import authorizationServer.security.handler.logoutHandler.MyLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import java.util.function.Function;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SpringSecurityConfigure {
    final CustomUserInfoMapper customUserInfoMapper;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService(DBUserDetailsManager dbUserDetailsManager) {
        return dbUserDetailsManager;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer();
        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (authorizationServer) ->
                        authorizationServer.oidc(oidc -> oidc
                                        .userInfoEndpoint(userinfo ->
                                                userinfo.userInfoMapper(customUserInfoMapper)
                                        )
                        )
                )
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .anyRequest().authenticated()
                );
        http.exceptionHandling(exception ->
                        exception
                                //.authenticationEntryPoint(new MyAuthenticationEntryPoint())
                                .accessDeniedHandler(new MyAccessDeniedHandler()));


        http.formLogin(login ->
                login
                        .loginPage("/login")
                        .permitAll()
                        .usernameParameter("userid")
                        .passwordParameter("password")
                        .successHandler(new MyAuthenticationSuccessHandler())
                        .failureHandler(new MyAuthenticationFailureHandler())
        );
//        http.logout(logout ->
//                logout.permitAll()
//                        .logoutSuccessHandler(new MyLogoutSuccessHandler())
//        );

//        );
        //http.addFilterBefore(new ParseJwtFilter(), UsernamePasswordAuthenticationFilter.class);
        //http.addFilterAfter(new SetJwtFilter(), UsernamePasswordAuthenticationFilter.class);

        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);


        return http.build();
    }


    @Bean//重新配置防火墙过滤器，允许url带有特殊字符
    public HttpFirewall allowUrlSemicolonHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true);
        return firewall;
    }

}
