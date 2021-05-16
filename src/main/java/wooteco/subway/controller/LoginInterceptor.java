package wooteco.subway.controller;

import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.infrastructure.AuthorizationExtractor;
import wooteco.subway.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public LoginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        String token = AuthorizationExtractor.extract(request);
        authService.validate(token);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
