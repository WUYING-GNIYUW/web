package com.wuying.gatewayServer.security;
import com.wuying.gatewayServer.security.filter.BarerProcessFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.server.adapter.ForwardedHeaderTransformer;


@EnableWebFluxSecurity
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SpringSecurityConfigure {
    private final BarerProcessFilter barerProcessFilter;
    @Bean
    public SecurityWebFilterChain defaultSecurityFilterChain(ServerHttpSecurity http) {

        // 禁用csrf与cors
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable);

        // 开启全局验证
        http
                .authorizeExchange((authorize) -> authorize
                .pathMatchers(
                        "/api/**","error/**","/favicon.ico","/oauth2/**","/userinfo/**","/connect/**","/public/**"
                ).permitAll()
                .anyExchange().authenticated()
        );
//        http.oauth2Login(oauth2Login -> oauth2Login
//                .authenticationSuccessHandler(new CustomAuthenticationSuccessHandler(requestCache))
//                .authenticationFailureHandler(new CustomAuthenticationFailureHandler())
//
//        ).requestCache(Cache -> Cache.
//                requestCache(requestCache));
        http
                .oauth2Login(Customizer.withDefaults())
                .oauth2Client(Customizer.withDefaults()).requestCache(Cache ->Cache.requestCache(new WebSessionServerRequestCache()));

//        http.exceptionHandling(exception -> exception
//                //.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
//                .accessDeniedHandler(new CustomAccessDeniedHandler())// 未认证用户访问受保护资源.accessDeniedHandler(accessDeniedHandler)       // 已认证用户权限不足
//        );
        http.addFilterAt(barerProcessFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }

//    @Bean
//    ForwardedHeaderFilter forwardedHeaderFilter() { return new ForwardedHeaderFilter(); }

}
