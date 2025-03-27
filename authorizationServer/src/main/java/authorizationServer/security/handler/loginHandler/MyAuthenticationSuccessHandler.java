package authorizationServer.security.handler.loginHandler;

import authorizationServer.thread.UseridThread;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.io.IOException;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;


public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UseridThread.set(request.getParameter("userid"));
        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        String targetUrl = (savedRequest != null) ? savedRequest.getRedirectUrl() : "/home";
        System.out.println(targetUrl);
        // 进行重定向
        response.setContentType("text/html;charset=utf-8");
        response.sendRedirect(targetUrl);
//        String jsonResult = JSONObject.toJSONString(Result
//                .<String>builder()
//                .code(null)
//                .message("4")
//                .data(null)
//                .build());
        //response.getWriter().println((jsonResult));
    }
}
