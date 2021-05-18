package wooteco.subway.auth.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.exception.EmptyTokenException;
import wooteco.subway.exception.InvalidTokenException;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    private final AuthService authService;

    public LoginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        String token = AuthorizationExtractor.extract(request);

        if (token == null) {
            throw new EmptyTokenException();
        }

        if (!authService.isValidToken(token)) {
            throw new InvalidTokenException();
        }

        return true;
    }

}
