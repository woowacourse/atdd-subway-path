package wooteco.subway.auth.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.exception.EmptyTokenException;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = AuthorizationExtractor.extract(request);

        if (accessToken == null) {
            throw new EmptyTokenException();
        }

        return true;
    }
}
