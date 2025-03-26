package security.controller;
import security.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@Controller
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping
public class LoginController {
    private final UserServiceImpl userServiceImpl;
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/info")
    public String info(HttpServletRequest request, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client, @AuthenticationPrincipal OAuth2User user) {
        request.setAttribute("userName", user.getName());
        request.setAttribute("clientName", client.getClientRegistration().getClientName());
        request.setAttribute("userAttributes", user.getAttributes());
        return "info";//返回userinfo.html
    }
}
