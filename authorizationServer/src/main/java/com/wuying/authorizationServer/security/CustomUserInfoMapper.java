//package authorizationServer.security;
//
//import cn.hutool.core.bean.BeanUtil;
////import common.feign.clients.UserClient;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
//import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
//import common.pojo.User;
//import org.springframework.stereotype.Component;
//
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//
//@Component
//@RequiredArgsConstructor(onConstructor_ = {@Autowired})
//public class CustomUserInfoMapper implements Function<OidcUserInfoAuthenticationContext, OidcUserInfo> {
//    //TODO private final UserClient userClient;
//    @Override
//    public OidcUserInfo apply(OidcUserInfoAuthenticationContext context) {
//        // 从上下文获取用户标识
//        String userId = context.getAuthorization().getPrincipalName();
//        //List<User> users = userClient.getUsers(User.builder().userId(userId).build()).getData();
////        Map<String,Object> userMap = BeanUtil.beanToMap(users.get(0));
////        Claims claims = Jwts.claims().setSubject("name");
//////                .subject("user123")
//////                .add("userId", 12345)       // 自定义字段
//////                .add("email", "user@example.com")
//////                .build();
//        Map<String,Object> userMap = new HashMap<>();
//        userMap.put("iss", "https://authorizationserver:8081");
//        userMap.put("sub", "name");
//        userMap.put("aud", "s6BhdRkqt3");
//        userMap.put("sub", "name");
//        userMap.put("sub", "name");
//        userMap.put("sub", "name");
//        userMap.put("sub", "name");
//        userMap.put("sub", "name");
//        userMap.put("pwd", "password");
//
//        return OidcUserInfo.builder().claims(claims -> claims.putAll(userMap)).build();
//    }
//}