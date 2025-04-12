package gatewayServer.security;
import gatewayServer.security.exceptionHandler.CustomAccessDeniedHandler;
import gatewayServer.security.exceptionHandler.CustomAuthenticationEntryPoint;
import gatewayServer.security.loginHandler.CustomAuthenticationFailureHandler;
import gatewayServer.security.loginHandler.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;


@EnableWebFluxSecurity
@Configuration
public class SpringSecurityConfigure {
    @Bean
    public SecurityWebFilterChain defaultSecurityFilterChain(ServerHttpSecurity http) {

        WebSessionServerRequestCache requestCache = new WebSessionServerRequestCache();
        // 禁用csrf与cors
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.cors(ServerHttpSecurity.CorsSpec::disable);

        // 开启全局验证
        http.authorizeExchange((authorize) -> authorize
                //TODO requestMatcher(forWebFlux)
                .anyExchange().authenticated()
        ).httpBasic(Customizer.withDefaults());

        http.oauth2Login(oauth2Login -> oauth2Login
                .authenticationSuccessHandler(new CustomAuthenticationSuccessHandler(requestCache))
                .authenticationFailureHandler(new CustomAuthenticationFailureHandler())
        ).requestCache(Cache -> Cache.
                requestCache(requestCache));
        http.exceptionHandling(exception -> exception.
                authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler())// 未认证用户访问受保护资源.accessDeniedHandler(accessDeniedHandler)       // 已认证用户权限不足
        );
        http.cors(Customizer.withDefaults());
        http.csrf(Customizer.withDefaults());
        return http.build();
    }
//        http.formLogin(login ->
//                login
//                        .loginPage("/login")
//                        .permitAll()
//                        .usernameParameter("userid")
//                        .passwordParameter("password")
//                        .successHandler(new MyAuthenticationSuccessHandler())
//                        .failureHandler(new MyAuthenticationFailureHandler())
//        );
//        http.logout(logout ->
//                logout.permitAll()
//                        .logoutSuccessHandler(new MyLogoutSuccessHandler())
//        );

        //http.addFilterBefore(new ParseJwtFilter(), UsernamePasswordAuthenticationFilter.class);
        //http.addFilterAfter(new SetJwtFilter(), UsernamePasswordAuthenticationFilter.class);


    @Bean//重新配置防火墙过滤器，允许url带有特殊字符
    public HttpFirewall allowUrlSemicolonHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true);
        return firewall;
    }
}
