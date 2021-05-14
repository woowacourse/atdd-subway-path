package wooteco.member.controller.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.member.service.AuthService;


public class SignInInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public SignInInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = request.getHeader("Authorization");
        authService.validateToken(accessToken);
        return true;
    }
}
