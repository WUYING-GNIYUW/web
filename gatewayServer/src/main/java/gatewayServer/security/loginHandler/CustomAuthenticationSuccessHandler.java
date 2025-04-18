package gatewayServer.security.loginHandler;


import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

public class CustomAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final ServerRequestCache requestCache;

    public CustomAuthenticationSuccessHandler(ServerRequestCache requestCache) {
        this.requestCache = requestCache;
    }
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        System.out.println("4");
        return requestCache.getRedirectUri(webFilterExchange.getExchange())
                .defaultIfEmpty(URI.create("/home")) // 无缓存时跳转默认页
                .flatMap(redirectUri -> {
                    exchange.getResponse().setStatusCode(null);//HttpStatus.FOUND
                    exchange.getResponse().getHeaders().setLocation(redirectUri);
                    return webFilterExchange.getExchange().getResponse().setComplete();
                });
    }
}
