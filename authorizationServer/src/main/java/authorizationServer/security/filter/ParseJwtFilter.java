package authorizationServer.security.filter;

import authorizationServer.thread.UseridThread;
import authorizationServer.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class ParseJwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String JwtToken = request.getHeader("authentication");
        if(JwtToken != null && !JwtToken.isEmpty()){
            Claims claims = JwtUtil.getClamis(JwtToken);
            String userid = claims.getSubject();
            Collection<GrantedAuthority> authorities = Arrays
                    .stream(claims.get("authorities", String.class).split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userid, null, authorities));
            UseridThread.set(userid);
        }
        filterChain.doFilter(request,response);

    }
}
