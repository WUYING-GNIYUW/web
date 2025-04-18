package gatewayServer.security.loginHandler;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CustomAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {
    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        System.out.println("3");
        return Mono.fromRunnable(() -> {
            String errorMsg = "登录失败: " + exception.getMessage();
            exchange.getResponse().setStatusCode(null);
            exchange.getResponse().getHeaders().setLocation(URI.create("/login?error=" + URLEncoder.encode(errorMsg, StandardCharsets.UTF_8))
            );
        });
    }
}