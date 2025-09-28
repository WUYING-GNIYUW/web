package com.wuying.authorizationServer.security.handler.loginHandler;

import com.wuying.authorizationServer.thread.UseridThread;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
//import org.redisson.api.RMap;
//import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.io.IOException;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//    private final RedissonClient redisson;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
//        RMap<String, String> map = redisson.getMap("userinfo:");
//        map.put("field1", "value1");
//        map.put("field2", "value2");
        userDetails.getUsername();
        userDetails.getAuthorities();
//        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
//        String targetUrl = (savedRequest != null) ? savedRequest.getRedirectUrl() : "/home";
//        System.out.println(targetUrl);
        // 进行重定向
        System.out.println("success");
        response.setContentType("text/html;charset=utf-8");
//        response.sendRedirect(targetUrl);
//        String jsonResult = JSONObject.toJSONString(Result
//                .<String>builder()
//                .code(null)
//                .message("4")
//                .data(null)
//                .build());
        //response.getWriter().println((jsonResult));
    }
}
