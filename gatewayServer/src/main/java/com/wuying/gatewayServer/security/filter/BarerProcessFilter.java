package com.wuying.gatewayServer.security.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class BarerProcessFilter implements WebFilter {
    private final ServerOAuth2AuthorizedClientRepository authorizedClientRepository;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 从 Reactor SecurityContext 获取 Authentication（必须是已认证用户）
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication())
                .flatMap(auth -> {
                    // 尝试从 Authentication 推断 registrationId（如果是 OAuth2AuthenticationToken）
                    String registrationId = null;
                    if (auth instanceof OAuth2AuthenticationToken) {
                        registrationId = ((OAuth2AuthenticationToken) auth).getAuthorizedClientRegistrationId();
                    }
                    // 如果不能推断 registrationId，你需要用别的方式拿到它（例如固定 client id 或从 request path/参数）
                    if (registrationId == null) {
                        return chain.filter(exchange); // 无法确定 client，直接继续
                    }
                    // 使用 authorizedClientRepository 加载 OAuth2AuthorizedClient（它会去 session / repo 查找）
                    return authorizedClientRepository.loadAuthorizedClient(registrationId, auth, exchange)
                            .flatMap(authorizedClient -> {
                                if (authorizedClient == null || authorizedClient.getAccessToken() == null) {
                                    return chain.filter(exchange);
                                }
                                String token = authorizedClient.getAccessToken().getTokenValue();
                                System.out.println(token);
                                //System.out.println(token);
                                // 把 token 放到 Authorization header 继续传递（或用于认证）
                                ServerHttpRequest mutated = exchange.getRequest().mutate()
                                        .header("Authorization", "Bearer " + token)
                                        .build();
                                ServerWebExchange mutatedExchange = exchange.mutate().request(mutated).build();
                                return chain.filter(mutatedExchange);
                                //return chain.filter(exchange);
                            })
                            .switchIfEmpty(chain.filter(exchange));
                })
                .switchIfEmpty(chain.filter(exchange)); // 如果没有 SecurityContext，直接继续
    }
}
