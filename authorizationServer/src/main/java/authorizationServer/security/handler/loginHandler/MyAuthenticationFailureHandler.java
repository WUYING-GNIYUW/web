package authorizationServer.security.handler.loginHandler;

import com.alibaba.fastjson2.JSONObject;
import common.pojo.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String b = request.getParameter("userId");
        String jsonResult = JSONObject.toJSONString(Result
                .<String>builder()
                .code(null)
                .message("authentication failed")
                .data(null)
                .build());
        System.out.println("fail");
        System.out.println(b);
        response.sendRedirect("/Login");
        response.setContentType("text/html;charset=utf-8");
    }
}
