package gatewayServer.security;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;


@EnableWebFluxSecurity
@Configuration
public class SpringSecurityConfigure {
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

        http.oauth2Login(Customizer.withDefaults());
        http.cors(Customizer.withDefaults());
        http.csrf(Customizer.withDefaults());
        return http.build();
    }
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(authorize ->
//                        authorize
//                                //.requestMatchers("/**").hasRole("ADMIN")
//                                .anyRequest().authenticated()//.permitAll()//
//
//                )
//                //.formLogin(Customizer.withDefaults())
//                .httpBasic(Customizer.withDefaults());
//
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
//        http.exceptionHandling(exception ->
//                exception
//                        //.authenticationEntryPoint(new MyAuthenticationEntryPoint())
//                        .accessDeniedHandler(new MyAccessDeniedHandler())
//        );
        //http.addFilterBefore(new ParseJwtFilter(), UsernamePasswordAuthenticationFilter.class);
        //http.addFilterAfter(new SetJwtFilter(), UsernamePasswordAuthenticationFilter.class);

//    }

    @Bean//重新配置防火墙过滤器，允许url带有特殊字符
    public HttpFirewall allowUrlSemicolonHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true);
        return firewall;
    }
}
