package security.util;

import security.constant.UserConstant;
import security.pojo.User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class OAuth2UserConverterUtil {
    private static final UserConstant userConstant;
    static {
        userConstant = (UserConstant) SpringContextUtil.getBean(UserConstant.class);
    }
    public static User convert(OAuth2User oAuth2User, OAuth2UserRequest oAuth2UserRequest){
        if(oAuth2UserRequest.getClientRegistration().getRegistrationId().equals("github")){
            Map<String, Object> attributes = oAuth2User.getAttributes();
            //        OAuth2AccessToken accessToken = oAuth2UserRequest.getAccessToken();
//        account.setCredentials(accessToken.getTokenValue());
//        ZonedDateTime zonedDateTime = accessToken.getExpiresAt().atZone(ZoneId.systemDefault());
//        account.setCredentialsExpiresAt(Date.from(zonedDateTime.toInstant()));
            return User.builder()
                    .userid(attributes.get("id").toString())
                    .origin(oAuth2UserRequest.getClientRegistration().getRegistrationId())
                    .build();
        }
        return null;
    }
}
