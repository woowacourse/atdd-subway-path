package wooteco.member.controller.config;

import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.member.infrastructure.AuthorizationExtractor;
import wooteco.member.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SignInInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public SignInInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = AuthorizationExtractor.extract(request);
        authService.validateToken(accessToken);
        return true;
    }
}
