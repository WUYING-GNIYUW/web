package authorizationServer.security.handler.exceptionHandler;

import com.alibaba.fastjson2.JSONObject;
import authorizationServer.pojo.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String jsonResult = JSONObject.toJSONString(Result
                .<String>builder()
                .code(null)
                .message(authException.getMessage())
                .data(null)
                .build());
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println((jsonResult));
    }
}
