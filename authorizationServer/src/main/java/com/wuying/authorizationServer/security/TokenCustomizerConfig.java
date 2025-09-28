package com.wuying.authorizationServer.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class TokenCustomizerConfig {
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return (context) -> {
            // 只定制 access_token（不影响 id_token）
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                // 1) 基于认证主体添加自定义 claim
                Authentication principal = context.getPrincipal();
                // 举例：把用户 id 加入 token（如果 principal 是 UsernamePasswordAuthenticationToken 并且 principal.getName() 是 userId）
                String userId = principal.getName(); // 根据你的 UserDetails 改取 userId
                context.getClaims().claims(claims -> {
                    claims.put("user_id", userId);

                    // 2) 把 authorities 放入 claims（去掉前缀或按照你的期望格式）
                    //principal.
                    Set<String> authorities = principal.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toSet());
//                    principal.getAuthorities().stream()
//                            .map(GrantedAuthority::getAuthority)
//                            .forEach(System.out::println);
                    claims.put("roles", authorities);

//                    // 3) 可加入来自 RegisteredClient 的信息
//                    var registeredClient = context.getRegisteredClient();
//                    claims.put("client_id", registeredClient.getClientId());
//
//                    // 4) 条件性添加：仅针对某个 client
//                    if ("trusted-client".equals(registeredClient.getClientId())) {
//                        claims.put("is_trusted_client", true);
//                    }
                });
//
//                // 5) 如果需要修改 JWT header（比如添加 kid 以外的自定义 header）
//                context.getHeaders().header("x-custom-header", "value");
            }
        };
    }
}