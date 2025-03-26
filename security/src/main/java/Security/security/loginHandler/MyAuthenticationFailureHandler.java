package Security.security.loginHandler;

import com.alibaba.fastjson2.JSONObject;
import Security.pojo.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String jsonResult = JSONObject.toJSONString(Result
                .<String>builder()
                .code(null)
                .message("3")
                .data(null)
                .build());
        System.out.println(exception.getMessage());
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println((jsonResult));
    }
}
