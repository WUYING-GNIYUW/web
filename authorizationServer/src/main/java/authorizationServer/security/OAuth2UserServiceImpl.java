package authorizationServer.security;

import authorizationServer.pojo.User;
import authorizationServer.service.UserService;
import authorizationServer.util.OAuth2UserConverterUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        User user = OAuth2UserConverterUtil.convert(oAuth2User, oAuth2UserRequest);
        if(user != null){
            if(userService.getById(user.getUserid()) == null){
                userService.save(user);
                System.out.println("插入成功");
            }
            else{
                System.out.println("用户已存在");
            }
        }
        return oAuth2User;
    }
}
