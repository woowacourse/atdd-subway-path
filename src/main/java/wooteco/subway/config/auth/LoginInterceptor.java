package wooteco.subway.config.auth;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.config.auth.infrastructure.AuthorizationExtractor;
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
        if (isPreflighted(request)) {
            return true;
        }
        String token = AuthorizationExtractor.extract(request);
        authService.validate(token);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private boolean isPreflighted(HttpServletRequest request) {
        String optionMethod = HttpMethod.OPTIONS.name();
        String requestMethod = request.getMethod();
        return optionMethod.equals(requestMethod);
    }
}
