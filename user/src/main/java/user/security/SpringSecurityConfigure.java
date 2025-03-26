package user.security;



import user.security.loginHandler.MyAuthenticationFailureHandler;
import user.security.loginHandler.MyAuthenticationSuccessHandler;
import user.security.exceptionHandler.MyAccessDeniedHandler;
import user.security.logoutHandler.MyLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SpringSecurityConfigure {
    private final DefaultOAuth2UserService oAuth2UserService;
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

}
