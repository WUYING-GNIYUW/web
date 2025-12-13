package com.wuying.authorizationServer.security.handler.exceptionHandler;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.wuying.common.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final Environment env;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        RequestCache requestCache = new HttpSessionRequestCache();
        requestCache.saveRequest(request, response);

        String redirectUrl = "/public/getResource?type=static&format=html&resource_name=loginPage&file_extension=html";
        response.sendRedirect(redirectUrl);
        response.setContentType("text/html;charset=utf-8");
        NamingService namingService = null;
        try {
            namingService = Util.getNamingService(env);
            List<Instance> allInstances = namingService.getAllInstances(env.getProperty("spring.application.name"));
            Optional<Instance> firstInstance = allInstances.stream()
                    .filter(i ->
                            i.getIp().equals(env.getProperty("spring.cloud.nacos.discovery.ip")))
                    .findFirst();
            Instance instance = firstInstance.orElseThrow(RuntimeException::new);

            Cookie cookie = new Cookie("sc-lb-itc-id", "Error in set stickyCookie");
            if (instance.getInstanceId() != null) {
                cookie = new Cookie("sc-lb-itc-id", instance.getInstanceId());
                cookie.setPath("/");
                cookie.setMaxAge(6);
                cookie.setHttpOnly(true);
                response.addCookie(cookie);
            } else {
                System.out.println("Error");
            }
            response.addCookie(cookie);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }


    }
}
