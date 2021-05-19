package wooteco.subway.auth.ui;

import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public LoginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = AuthorizationExtractor.extract(request);
        if (request.getMethod().equals("OPTIONS")) {
            return false;
        }
        authService.validateToken(accessToken);
        return true;
    }
}
