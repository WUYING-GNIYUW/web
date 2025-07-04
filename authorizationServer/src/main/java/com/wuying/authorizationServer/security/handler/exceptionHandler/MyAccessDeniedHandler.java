package com.wuying.authorizationServer.security.handler.exceptionHandler;

import com.alibaba.fastjson2.JSONObject;
import com.wuying.common.pojo.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String jsonResult = JSONObject.toJSONString(Result
                .<String>builder()
                .code(null)
                .message("denied")
                .data(null)
                .build());
        System.out.println(accessDeniedException.getCause());
        System.out.println(accessDeniedException.getMessage());
        response.setContentType("text/html;charset=utf-8");
    }
}
