package wooteco.member.controller.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.member.service.AuthService;


public class LonginInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public LonginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getHeader("Authorization");
        authService.validateToken(accessToken);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
