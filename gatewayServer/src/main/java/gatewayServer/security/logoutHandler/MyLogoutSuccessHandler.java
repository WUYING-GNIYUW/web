package gatewayServer.security.logoutHandler;

import com.alibaba.fastjson2.JSONObject;
import common.pojo.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String jsonResult = JSONObject.toJSONString(Result
                .<String>builder()
                .code(null)
                .message(null)
                .data(null)
                .build());

        response.setContentType("text/html;charset=utf-8");
    }
}
