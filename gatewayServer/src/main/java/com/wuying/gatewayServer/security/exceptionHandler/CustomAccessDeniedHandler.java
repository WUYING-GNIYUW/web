package com.wuying.gatewayServer.security.exceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.net.URI;

public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        // 1. 获取 Referer 头（来源页面 URL）
        String referer = exchange.getRequest().getHeaders().getFirst("Referer");
        System.out.println("1");
        // 2. 验证 Referer 是否有效（防止开放重定向漏洞）
        if (isValidReferer(referer)) {
            exchange.getResponse().setStatusCode(null);
            exchange.getResponse().getHeaders().setLocation(URI.create(referer));
        } else {
            exchange.getResponse().setStatusCode(null);
            exchange.getResponse().getHeaders().setLocation(URI.create("/home"));
        }
        return exchange.getResponse().setComplete();
    }

    // 验证 Referer 是否属于当前域名
    private boolean isValidReferer(String referer) {
        return referer != null &&
                referer.startsWith("https://your-domain.com"); // 替换为实际域名
    }


}