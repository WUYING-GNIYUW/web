package com.wuying.authorizationServer.security.handler.loginHandler;

import cn.hutool.core.bean.BeanUtil;
import com.wuying.common.pojo.UserInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final RedissonClient redissonClient;
    private final SavedRequestAwareAuthenticationSuccessHandler defaultHandler = new SavedRequestAwareAuthenticationSuccessHandler();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        String userId = ((UserDetails)authentication.getPrincipal()).getUsername();
        RMap<String, Object> userMap = redissonClient.getMap("user:" + userId);
//            userMap.put("instanceId", instance.getInstanceId());
//            userMap.put("ip", instance.getIp());
//            userMap.put("port", instance.getPort());
//            userMap.put("serviceName", instance.getServiceName());
        userMap.expire(Duration.ofHours(12));

        Optional<Cookie> scCookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals("sc-lb-itc-id")).findFirst();
        Cookie cookie = scCookie.orElseThrow(RuntimeException::new);

        UserInfo userInfo = UserInfo.builder().userId(userId).authorizationInstanceId(cookie.getValue()).build();
        Map<String,Object> userInfoMap = BeanUtil.beanToMap(userInfo);
        userMap.putAll(userInfoMap);

        cookie = new Cookie("sc-lb-itc-id", "Error in destroy stickyCookie");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        log.atInfo().log("CustomAuthenticationSuccessHandler worked");
        defaultHandler.onAuthenticationSuccess(request, response, authentication);

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
