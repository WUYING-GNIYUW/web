package com.wuying.authorizationServer.security.handler.exceptionHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        RequestCache requestCache = new HttpSessionRequestCache();
        requestCache.saveRequest(request, response);

        String redirectUrl = "/public/getResource?type=static&format=html&resource_name=loginPage&file_extension=html";
        response.sendRedirect(redirectUrl);
        response.setContentType("text/html;charset=utf-8");

    }
}
