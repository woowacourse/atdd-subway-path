package wooteco.subway.auth.ui;

import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public AuthInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String token = AuthorizationExtractor.extract(request);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException();
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
