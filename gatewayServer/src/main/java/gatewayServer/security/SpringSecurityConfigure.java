package gatewayServer.security;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@Configuration
public class SpringSecurityConfigure {

    /**
     * 配置认证相关的过滤器链
     *
     * @param http Spring Security的核心配置类
     * @return 过滤器链
     */
    @Bean
    public SecurityWebFilterChain defaultSecurityFilterChain(ServerHttpSecurity http) {
        // 禁用csrf与cors
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.cors(ServerHttpSecurity.CorsSpec::disable);

        // 开启全局验证
        http.authorizeExchange((authorize) -> authorize
                //TODO requestMatcher(forWebFlux)
                .anyExchange().authenticated()
        ).httpBasic(Customizer.withDefaults());

        // 开启OAuth2登录
        http.oauth2Login()
                .


        // 设置当前服务为资源服务，解析请求头中的token
        http.oauth2ResourceServer((resourceServer) -> resourceServer
                        // 使用jwt
                        .jwt(jwt -> jwt
                                // 请求中携带token访问时会触发该解析器适配器
                                .jwtAuthenticationConverter(grantedAuthoritiesExtractor())
                        )
                /*
                // xhr请求未携带Token处理
                .authenticationEntryPoint(this::authenticationEntryPoint)
                // 权限不足处理
                .accessDeniedHandler(this::accessDeniedHandler)
                // Token解析失败处理
                .authenticationFailureHandler(this::failureHandler)
                */
        );

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize ->
                        authorize
                                //.requestMatchers("/**").hasRole("ADMIN")
                                .anyRequest().authenticated()//.permitAll()//

                )
                //.formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        http.formLogin(login ->
                login
                        .loginPage("/login")
                        .permitAll()
                        .usernameParameter("userid")
                        .passwordParameter("password")
                        .successHandler(new MyAuthenticationSuccessHandler())
                        .failureHandler(new MyAuthenticationFailureHandler())
        );
        http.logout(logout ->
                logout.permitAll()
                        .logoutSuccessHandler(new MyLogoutSuccessHandler())
        );
        http.exceptionHandling(exception ->
                exception
                        //.authenticationEntryPoint(new MyAuthenticationEntryPoint())
                        .accessDeniedHandler(new MyAccessDeniedHandler())
        );
        //http.addFilterBefore(new ParseJwtFilter(), UsernamePasswordAuthenticationFilter.class);
        //http.addFilterAfter(new SetJwtFilter(), UsernamePasswordAuthenticationFilter.class);
        http.oauth2Login(oauth -> oauth
                .loginPage("/login")
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(oAuth2UserService)));
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

    /**
     * 自定义jwt解析器，设置解析出来的权限信息的前缀与在jwt中的key
     *
     * @return jwt解析器适配器 ReactiveJwtAuthenticationConverterAdapter
     */
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // 设置解析权限信息的前缀，设置为空是去掉前缀
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        // 设置权限信息在jwt claims中的key
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
