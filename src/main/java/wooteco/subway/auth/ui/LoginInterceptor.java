package wooteco.subway.auth.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.exception.NotEmptyTokenException;
import wooteco.subway.exception.NotValidToken;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    private final AuthService authService;

    public LoginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        String token = AuthorizationExtractor.extract(request);

        if (token == null) {
            throw new NotEmptyTokenException();
        }

        if (!authService.isValidToken(token)) {
            throw new NotValidToken();
        }

        return super.preHandle(request, response, handler);
    }

}
